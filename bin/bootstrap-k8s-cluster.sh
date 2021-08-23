#!/bin/bash
export podName="[PodName]"
export k8sNamespace="default"
export VAULT_TOKEN=`grep 'Initial Root Token'  /var/vault/config/init.keys | awk '{print $NF}'`
export API_SERVER="https://kubernetes.docker.internal:6443" # get the value using `kubectl config view` command. Navigate to docker-desktop. select path `.cluster.server`
vault auth enable kubernetes # you can ignore the error path is already in use at kubernetes

echo creating vault-auth service account in default namespace
cat > vault-auth-sa.yaml<<EOF
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: vault-auth
  namespace: default
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: role-tokenreview-binding
  namespace: default
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: system:auth-delegator
subjects:
  - kind: ServiceAccount
    name: vault-auth
    namespace: default
---
EOF
kubectl apply -f vault-auth-sa.yaml

export VAULT_SA_NAME=$(kubectl get sa vault-auth -o jsonpath="{.secrets[*]['name']}")
export SA_JWT_TOKEN=$(kubectl get secret $VAULT_SA_NAME -o jsonpath="{.data.token}" | base64 --decode; echo)

# Set SA_CA_CRT to the PEM encoded CA cert used to talk to Kubernetes API
export SA_CA_CRT=$(kubectl get secret $VAULT_SA_NAME -o jsonpath="{.data['ca\.crt']}" | base64 --decode; echo)

# create vault/K8s association
vault write auth/kubernetes/config \
token_reviewer_jwt="$SA_JWT_TOKEN" \
kubernetes_host="$API_SERVER" \
kubernetes_ca_cert="$SA_CA_CRT"
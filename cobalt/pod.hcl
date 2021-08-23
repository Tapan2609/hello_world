job "{{ $job_name }}" {
    [[ file "service.hcl" ]]
    [[ file "job.common.hcl" ]]

    group "nginx" {
        [[ file "group.common.hcl" ]]
        count = {{ $nginx_count}}

        task "nginx" {
            {{ executeTemplate "service" ",true,false,,true,false,443,http,443,/health.html,https,30s,10s," }}
            [[ file "task.common.hcl" ]]

            config {
                image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/nginx:{{ $build_number }}"
                network_mode = "{{ $nginx_nw_mode }}"
                [[ file "registry.auth.hcl" ]]
            }

            env {
                [[ file "env.hcl" ]]
                # By specifying `0` we make nginx forward the / itself to this service.
                # In all normal cases you will start with PROXIED_SERVICE_1
                PROXIED_SERVICE_0="greeting"
                PROXIED_SERVICE_1="test"
                CONTAINERPILOT_LOG_LEVEL="INFO"
                ENABLE_NOMAD_REGISTRATION="true"
            }

            resources {
                cpu    = {{ $nginx_cpu }} # MHz
                memory = {{ $nginx_memory }} # MB
                [[ file "network.common.hcl" ]]
            }
        }
    }

    group "greeting" {
        [[ file "group.common.hcl" ]]
        count = {{ $greeting_count }}

        task "greeting" {
            {{ executeTemplate "service" ",false,false,,true,false,443,http,443,/greeting,https,30s,10s," }}
            [[ file "task.common.hcl" ]]
            config {
                image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/greeting:{{ $build_number }}"
                network_mode = "{{ $greeting_nw_mode }}"
                [[ file "registry.auth.hcl" ]]
                privileged = true
                userns_mode = "host"
            }

            env {
                [[ file "env.hcl" ]]
                # This is just to show the current orchestrator in UI
                # Do not copy this variable
                COBALT_ORCHESTRATOR="Nomad"
                {{ if $is_dev }}
                  WAIT_FOR_SERVICE="{{ printf "%s-nfs" $job_name }}"
                  NFS_SERVER="{{ printf "%s-nfs.service" $job_name }}"
                  NFS_MOUNT_PARAMS="rw,nfsvers=3,nolock,proto=tcp,port=2049"
                  NFS_SRC_PATH="/home/local"
                  NFS_MOUNT_PATH="/sampleapp"
                  #An else block is required for prod, leaving this empty as sampleapp mount path is not available in prod.
                  #Example vars to be used are shown below.
                  #WAIT_FOR_SERVICE="nfs.service"
                  #NFS_SRC_PATH = "/local"
                  #NFS_MOUNT_PATH = "/sampleapp"
                  LOG_LEVEL="debug"
                  CONTAINERPILOT_LOG_LEVEL="INFO"
                {{ end }}
                ENABLE_CODE_COVERAGE="{{ $enable_code_coverage }}"
                ENABLE_NOMAD_REGISTRATION="true"
            }

            resources {
                cpu    = {{ $greeting_cpu }} # MHz
                memory = {{ $greeting_memory }} # MB
                [[ file "network.common.hcl" ]]
            }
        }
    }

    group "test" {
        [[ file "group.common.hcl" ]]
        count = {{ $test_count }}

        task "test" {
            {{ executeTemplate "service" "${NOMAD_JOB_NAME}-test,true,false,,true,false,443,http,443,/v1/message,https,30s,10s," }}
            [[ file "task.common.hcl" ]]

            config {
                image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/test:{{ $build_number }}"
                network_mode = "{{ $test_nw_mode }}"
                [[ file "registry.auth.hcl" ]]
            }

            env {
                [[ file "env.hcl" ]]
                SERVICE_NAME="{{ printf "%s-test" $job_name }}"
                CONTAINERPILOT_LOG_LEVEL="INFO"
                ENABLE_NOMAD_REGISTRATION="true"
            }

            resources {
                cpu    = {{ $test_cpu }} # MHz
                memory = {{ $test_memory }} # MB
                [[ file "network.common.hcl" ]]
            }
        }
    }

    group "ftp" {
        [[ file "group.common.hcl" ]]
        count = {{ $ftp_count }}

        task "ftp" {
            {{ executeTemplate "service" ",false,false,custom_tag,true,false,21,tcp,21,,,30s,10s," }}
            [[ file "task.common.hcl" ]]

            config {
                image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/ftp:{{ $build_number }}"
                network_mode = "{{ $ftp_nw_mode }}"
                [[ file "registry.auth.hcl" ]]
            }

            env {
                [[ file "env.hcl" ]]
                CONTAINERPILOT_LOG_LEVEL="INFO"
                ENABLE_NOMAD_REGISTRATION="true"
            }

            resources {
                cpu    = {{ $ftp_cpu }} # MHz
                memory = {{ $ftp_memory }} # MB
                [[ file "network.common.hcl" ]]
            }
        }
    }

    group "int" {
        [[ file "group.common.hcl" ]]
        count = {{ $int_count }}

        task "int" {
            {{ executeTemplate "service" ",false,false,,true,false,443,http,443,/greeting,https,30s,10s," }}
            [[ file "task.common.hcl" ]]

            config {
                image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/greeting:{{ $build_number }}"
                network_mode = "{{ $int_nw_mode }}"
                [[ file "registry.auth.hcl" ]]
            }

            env {
                IS_INTERNAL = "true"
                [[ file "env.hcl" ]]
                LOG_LEVEL="debug"
                CONTAINERPILOT_LOG_LEVEL="INFO"
                ENABLE_NOMAD_REGISTRATION="true"
            }

            resources {
                cpu    = {{ $int_cpu }} # MHz
                memory = {{ $int_memory }} # MB
                [[ file "network.common.hcl" ]]
            }
        }
    }

    group "nfs" {
      [[ file "restart.hcl" ]]
      count = {{ $nfs_count }}

      ephemeral_disk {
        size    = "500"
      }

      task "nfs" {
        {{ executeTemplate "service" ",false,false,,true,false,2049,tcp,2049,,,30s,10s," }}
        [[ file "task.common.hcl"]]

        config {
          image        = "{{ env "DOCKER_REGISTRY" }}/{{ $product_path }}/nfs:{{ $build_number }}"
          network_mode = "{{ $nfs_nw_mode }}"
          [[ file "registry.auth.hcl" ]]
          privileged = true
          userns_mode = "host"
        }

        env {
          [[ file "env.hcl" ]]
          CONTAINERPILOT_LOG_LEVEL="INFO"
          ENABLE_NOMAD_REGISTRATION="true"
        }

        resources {
          cpu    = {{ $nfs_cpu }} # MHz
          memory = {{ $nfs_memory }} # MB
          [[ file "network.common.hcl"]]
        }
      }
    }
    meta {
        cs.jira.issue = "CS-38"
        product.name = "Sampleapp-java"
        product.descriptive-name = "Sampleapp cobalt application"
        product.family = "Samples"
        product.type = "internal"
        product.front-door-url = "/greeting"
        product.health-check-url = "/health1,/health2"
        contact.primary = "renjith.pillai@sap.com"
        contact.secondary = "balaji.srinivasan01@sap.com"
        contact.us = "sasanka.griddalur@sap.com"
        contact.idc = "sanjay.dc@sap.com"
        contact.team-dl = "DL_59B068535F99B786180000CF@sap.com"
        contact.slack-channel = "https://sap-ariba.slack.com/messages/C96L4NTTK"
        date.beta   = "31-Oct-2016"
        date.ga     = "30-Nov-2016"
        project.jira-project = "CBLT"
        doc.sre-cookbook = "https://wiki.ariba.com/display/COP"
        doc.project-wiki = "https://wiki.ariba.com/display/CDP"
        hana.schema = "schema1,schema2"
        datadog.dashboard = "https://app.datadoghq.com/screen/336489/cobalt-dashboard"
        remarks = <<REMARKS
                  This is a sample remarks section which
                  can span multiple lines. Use this section to add any
                  descriptive content for the Info sheet for the service.
        REMARKS
    }
}

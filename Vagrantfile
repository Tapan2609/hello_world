Vagrant.configure(2) do |config|
  # In future there will be a better way to abstract this from user
  class Target
      def to_s
          print "Enter the target. Possible options are 'k8s' or 'hashi'.\n"
          print "Target: "
          STDIN.gets.chomp
      end
  end
  config.vm.hostname = "sampleapp-java-host"
  config.vm.provider "docker" do |d|
      d.image       = "docker-dev-repo.cobalt.ariba.com/ariba-cobalt-vagrant/vagrant:V-88a9a9a-15" # update the image as per the CDP page
      d.has_ssh     = true
      d.privileged  = true
      d.pull        = false
      d.create_args = ["-v", "/var/run/docker.sock:/var/run/docker.sock", '--memory', '3g']
      d.ports       = ["8500:8500", "8200:8200", "4646:4646", "3500:3500", "8080:8080"]
end

  #Keep this to reuse the maven dependencies that are getting downloaded. Saves a lot of time
  config.vm.synced_folder "~/.m2", "/home/vagrant/.m2", docker_consistency: "delegated"

  # docker desktop config file and keys
  config.vm.synced_folder "~/.kube", "/var/tmp/.kube", docker_consistency: "delegated"

  # Sync up the project folder from laptop host
  config.vm.synced_folder ".", "/home/vagrant/sampleapp-java", docker_consistency: "consistent"

  #Prepare the machine, do not remove this block

  config.vm.provision :shell, env: {"TARGET" => Target.new}, inline: <<-EOF
     /usr/local/bin/post-startup.sh $TARGET
  EOF

  #Do the initial build of all components
  config.vm.provision :shell, privileged: false, inline: <<-EOF
    cd /home/vagrant/sampleapp-java
    ./bin/buildAll.sh $TARGET
  EOF

  config.vm.provision :shell,  inline: <<-EOF
    echo "Vagrant Box provisioned for ${TARGET}!"
    if [ "$TARGET" == "hashi" ]; then
      echo "Consul UI can be accessed at:  http://localhost:8500/ui"
      echo Once completed, the service can be accessed at the below links
      echo "      Sample app:  https://127.0.0.1/sampleapp-java-dev/greeting/" # You can use localhost instead of 127.0.0.1 if you are using latest vagrant image.
      echo "      Test Rest End point to start test:  https://127.0.0.1:9090/sampleapp-java-dev-test/v1/start"
    elif [ "$TARGET" == "k8s" ]; then
      echo Once completed, the service can be accessed at the below links
      echo "      Sample app:  https://localhost/ariba-sampleapp-java/greeting"
    fi
  EOF

  config.trigger.before  [:destroy, :halt] do |trigger|
    trigger.info = "Shutting down containers"
    trigger.run_remote = {inline: "/usr/local/bin/pre-shutdown.sh"}
  end
end

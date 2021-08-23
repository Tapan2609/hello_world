@Library('PipelineLibrary@develop') _

import com.sap.ms.*

def branch = env.BRANCH_NAME

def purposeParam = params.PURPOSE ?: "StandardPipeline"
def type = params.CLUSTER_TYPE ?: "hashi"



try {
    switch (purposeParam) {
        case "StandardPipeline":
            runStandardPipeline(branch, type)
            break
        case "FortifyMaven":
            runFortifyPipelineMaven()
            break
        case "MavenRelease":
            runMavenRelease()
            break
        default:
            error("Invalid purpose ${purposeParam}")
    }
} catch(Exception exe) {
    error("Pipeline failed [${purposeParam}], error [${exe.getMessage()}]")
}

def runStandardPipeline(branch, type) {
    def pTrigger = null
    def deployType = "hashi"
    if( type == "kubernetes" ) {
        deployType = "kubernetes"
        searchString = "This demostrates the canary deployment infrastructure based on docker images into the Kubernetes clusters"
    }
    if(branch == "master") {
        println "Master branch build, setting periodic trigger"
        pTrigger = "30 5,15 * * *"
    }

    def mvnArguments        = "clean package"
    def customPostDeploy    = null
    def sonarScan           = null
    def collectCoverage     = null
    def doTests               = false

    if(!isPeriodicTrigger{}) {
        println "Not a periodic trigger, doing extensive tests"
        mvnArguments        = "clean checkstyle:checkstyle package test"
        sonarScan           = ["greeting"]
        collectCoverage     = [""]
        doTests             = true
        customPostDeploy    = { serviceUrl ->
            def searchString="This demostrates the new deployment infrastructure"
            if( deployType == "kubernetes" ) {
                searchString = "This demostrates the canary deployment infrastructure based on docker images into the Kubernetes clusters"
            }
            withSauceLabs {
                println "With saucelabs environment..."
                mvnBuild {
                    path    = "selenium"
                    goals   = "test -DargLine=\"-DserviceUrl=${serviceUrl}/greeting -DtextToSearch='${searchString}'\""
                }
            }
        }
    } else {
        println "Periodic trigger, doing minimal build steps"
    }

    // Refer to https://github.wdf.sap.corp/Ariba-cobalt/pipeline-library/tree/master/README.md for
    // a description of all supported parameters
    standardPipeline {
        csid        = "CS-38"
        productPath = "ariba-sampleapp-java"
        queryName   = "sampleapp-java"
        podName     = "sampleapp-java"
        // Uncomment the below three lines for enabling gradle builds
        // customBuildStep = {
        //  sh "gradle build"
        // }
        customParameters  = {
            [
                [$class: 'ChoiceParameterDefinition', choices: ["StandardPipeline", "FortifyMaven", "MavenRelease"], 
                description: 'Choose the purpose of the run', name: 'PURPOSE'],
                [$class: 'ChoiceParameterDefinition', choices: ["hashi","kubernetes"],
                description: 'Choose the cluster type', name: 'CLUSTER_TYPE']
            ]
        }
        mvnBuilds = ["greeting": mvnArguments, "test": mvnArguments]
        dockerBaseImageVersion  = "e9e0eb2"
        dockerBuildPush         = ["greeting": "greeting", "test": "test","nginx": "nginx", "nfs":"nfs", "ftp":"ftp"]
        sonarScanPaths          = sonarScan
        if( deployType == "hashi" ) {
            collectContainerJacoco  = collectCoverage
            additionalDeployments = ["devfarm.cobalt.ariba.com", "e2e.cobalt.ariba.com"]
        }
        failBuildOnSonarScan    = doTests
        secure  = true
        appUid  = "2ef6193d-5750-45b9-8098-f98f4232dd3c"
        secrets = ["spring.datasource.password":"roomba123"]
        periodicTrigger     = pTrigger
        maxUploadSize       = "1024m"
        setUploadLimit      = doTests
        endPoint            = "greeting"
        doContainerTests    = doTests
        publishTestResults  = doTests
        publishCheckstyle   = doTests
          
        // JaCoCo specific parameters
        publishJacoco           = doTests
        jacocoClassPattern      = "**/target/classes"
        jacocoExecPattern       = "**/*.exec"
        jacocoSourcePattern     = "**/src/main/java"
        testngThresholdFailedFails    = 0.1
        testngThresholdFailedSkips    = 0.1
        testngThresholdUnstableFails  = 0.1
        testngThresholdUnstableSkips  = 0.1
        
        customPostMADStep = {
            sh "echo Today we die, for Sparta!"
        }

        customPostDeployStep = customPostDeploy

        productionReady     = true
        productionApprovers = "i857925,i304306,i851588"
        productionDelegates = "i345176,i345678"
        regions = ["US","EU","CN","RU","UAE","KSA","AU","JP","LAB"]
        

        slackChannel = 'cobalt-test'
        slackHookUrl = 'https://hooks.slack.com/services/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'
        slackNotification = true
        autoApprove       = true
        i18nCheck         = doTests
        emailRecipients   = "ankit.midha@sap.com"
        sendFailureMail   = true
        sendSuccessMail   = true
        emailCulprits     = true
        if( deployType == "kubernetes" ) {
            clusterName = "dev"
            clusterType = "kubernetes"
            helmCommand = "helm3"
            skipDeployment = false
            helmReleaseName  = "sampleapp-java"
            retainBranchDeployments=true
            retainBranchArtifacts=true
            publicArtifacts = false
            publishJacoco = false
            doContainerTests = false
            debug=true
            namespace = "family-sampleapp"
            landscape = "qa"
        }

    }
}

def runFortifyPipelineMaven() {
    node("fortify") {
        checkout scm
        def fortify = new Fortify()
        def status = fortify.standardScan("greeting", "clean", "sampleapp-java-0.1.0.fpr")
        if(status) {
                fortify.uploadFortifyResults("greeting/sampleapp-java-0.1.0.fpr", "TestApp", "1.0")
        } else {
            error("Fortify analysis failed")
        }
    }
}

def runMavenRelease() {
    node("swarm") {
        checkout scm
        mavenRelease{
            gitUrl        = "https://github.wdf.sap.corp/Ariba/sampleapp-java.git"
            goals         = "clean install"
            noBuildNumber = true
            projectPath   = "greeting"
        }
    }
}



def call (body) {
    def config = [:]
    body.resolveStrategy = closure.Delegate_First
    body.delegate = config
    body()
    
pipeline {
    
    environment {
        DockerImage = ""
    }
    
   
  agent any
 
  triggers { 
     githubPush()
  }

  stages {
     stage('SCM checkout') {
           steps {
              git credentialsId: "${config.SourceCredentials}", url: "${config.SourceRepo}"
           }
     }
   
     stage('checksum') {
         steps {
             script {
                 fingerprint '**/*.war'
             }
         }
     }
     stage('Upload to JFrog') { 
           steps {
             script {
                def server = Artifactory.newServer url: "${config.ArtifactoryUrl}", credentialsId: "${config.ArtifactoryCredentials}"
                def uploadSpec = """{
                                      "files": [
                                          {
                                             "pattern": "${config.WORKSPACE}/*.war",
                                             "target": "${config.VendorName}/${config.Product}/${config.Version}/"
                                          }
                                       ]
                                  }"""
                server.upload spec: uploadSpec
              }
            }
     }
     stage('Build image') {
         steps {
               script {
               dockerImage = docker.build "${config.Registry}" + ":$BUILD_NUMBER"
               
             }
         }
     }
     
     stage('Push Image') {
         steps {
             script {
                 docker.withRegistry( '', "${config.RegistryCredential}" ) {
                     dockerImage.push()
                 } 
             }
         }           
     }

  }
 
   post { 
        failure { 
            echo 'Build Pipeline NOK'
        }
    }
}

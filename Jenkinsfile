pipeline {
    
    environment {
        DockerImage = ""
    }
    
   def loadEnvironmentVariables(path) {
       def props = readProperties file: path
       keys= props.keyset()
       for (key in keys) {
           value = props["${key}"]
           env."${key}" = "${value}"
       }
   }
   
  agent any

  stages {
     stage('SCM checkout') {
           steps {
              git credentialsId: "${SourceCredentials}", url: "${SourceRepo}"
              loadEnvironmentVariables('CLIENT.properties')
           }
     }

     stage('Upload to JFrog') { 
           steps {
             script {
                def server = Artifactory.newServer url: "${ArtifactoryUrl}", credentialsId: "${ArtifactoryCredentials}"
                def uploadSpec = """{
                                      "files": [
                                          {
                                             "pattern": "${WORKSPACE}/*.war",
                                             "target": "${VendorName}/${Product}/${Version}/"
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
               dockerImage = docker.build "${Registry}" + ":$BUILD_NUMBER"
             }
         }
     }
     
     stage('Push Image') {
         steps {
             script {
                 docker.withRegistry( '', "${RegistryCredential}" ) {
                     dockerImage.push()
                 } 
             }
         }           
     }

  }
  
}

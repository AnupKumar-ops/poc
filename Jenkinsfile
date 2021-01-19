def loadEnvironmentVariables(path){
    def props = readProperties  file: path
    keys= props.keySet()
    for(key in keys) {
        value = props["${key}"]
        env."${key}" = "${value}"
    }
} 

pipeline {
    
    environment {
        SOURCE_CREDENTIAL = "github"
        SOURCE_REPO = "https://github.com/AnupKumar-ops/poc.git"
        DockerImage = ""
    }
   
  agent any

  stages {
     stage('SCM checkout') {
           steps {
              git credentialsId: "${SOURCE_CREDENTIAL}", url: "${SOURCE_REPO}"
           }  
     }
      
     stage('load variables') {
           steps {
              loadEnvironmentVariables("${WORKSPACE}/CLIENT.properties")
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
                def server = Artifactory.newServer url: "${ARTIFACTORY_URL}", credentialsId: "${ARTIFACTORY_CREDENTIALS}"
                def uploadSpec = """{
                                      "files": [
                                          {
                                             "pattern": "${WORKSPACE}/*.war",
                                             "target": "${VENDOR_NAME}/${PRODUCT}/${VERSION}/"
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
               dockerImage = docker.build "${REGISTRY}" + ":$BUILD_NUMBER"
             }
         }
     }
     
     stage('Push Image') {
         steps {
             script {
                 docker.withRegistry( '', "${REGISTRY_CREDENTIAL}" ) {
                     dockerImage.push()
                 } 
             }
         }           
     }

  }
  
}

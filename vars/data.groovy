#!/usr/bin/env groovy

def call() {
    echo "Hello"
}

def VendorName(){
    echo "Cisco"
}
def Product() {
    echo "WarFiles"
}
def Version() {
    echo "vnf_v1.1"
}
def ArtifactoryUrl() {
    echo "http://34.71.26.245:8082/artifactory"
}
def ArtifactoryCredentials() {
    echo "jfogid"
}
def App() {
    echo "app2" 
}        
def SourceRepo() {
    echo "https://github.com/AnupKumar-ops/jenkinsdemo"
}       
def SourceCredentials() {
    echo "github"
}
def Registry() {
    echo "docker.io/963287/myrepo"
}   
def RegistryCredential() {
    echo "docker"
}

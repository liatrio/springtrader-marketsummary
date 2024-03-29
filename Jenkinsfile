pipeline {
  agent { label "minimal" }
  stages {
    stage('Build') {
      agent {
        label "lead-toolchain-skaffold"
      }
      steps {
        container('skaffold') {
          sh "skaffold build --file-output=image.json"
          stash includes: 'image.json', name: 'build'
          sh "rm image.json"
        }
      }
    }

    stage("Deploy to Staging") {
      agent {
        label "lead-toolchain-skaffold"
      }
      when {
          branch 'master'
      }
      environment {
        ISTIO_DOMAIN     = "${env.stagingDomain}"
        PRODUCT_NAME     = "${env.product}"
        NODE_ENV         = "staging"
        DB_HOSTNAME      = "mongodb.${env.databaseNamespace}.svc.cluster.local"
        DB_PORT          = "27017"
        DB_DATABASE_NAME = "staging"
      }
      steps {
        container('skaffold') {
          unstash 'build'
          sh "skaffold deploy -a image.json -n ${env.stagingNamespace}"
          stageMessage "Successfully deployed to staging:\nspringtrader-${env.product}.${env.stagingDomain}/spring-nanotrader-services/api/marketSummary"
        }
      }
    }

    stage ('Manual Ready Check') {
      agent none
      when {
        branch 'master'
      }
      options {
        timeout(time: 10, unit: 'MINUTES')
      }
      input {
        message 'Deploy to Production?'
      }
      steps {
        echo "Deploying"
      }
    }

    stage("Deploy to Production") {
      agent {
        label "lead-toolchain-skaffold"
      }
      when {
          branch 'master'
      }
      environment {
        ISTIO_DOMAIN     = "${env.productionDomain}"
        PRODUCT_NAME     = "${env.product}"
        NODE_ENV         = "production"
        DB_HOSTNAME      = "mongodb.${env.databaseNamespace}.svc.cluster.local"
        DB_PORT          = "27017"
        DB_DATABASE_NAME = "production"
      }
      steps {
        container('skaffold') {
          unstash 'build'
          sh "skaffold deploy -a image.json -n ${env.productionNamespace}"
          stageMessage "Successfully deployed to production:\nspringtrader-${env.product}.${env.productionDomain}/spring-nanotrader-services/api/marketSummary"
        }
      }
    }
  }
}

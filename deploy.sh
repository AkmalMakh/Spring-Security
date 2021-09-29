../gradlew bootJar
scp -i AuthenticationEC2Server.pem build/libs/Authentication-0.0.1-SNAPSHOT.jar ec2-user@3.34.96.214:~/
ssh -i AuthenticationEC2Server.pem ec2-user@3.34.96.214 'sh deploy.sh'

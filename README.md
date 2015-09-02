# akka-actor-io  
### *The effect of I/O from actors*  
  
  
## Compile and Run - Transmitter  
  
`mvn clean compile`  
  
`mvn exec:java -Dexec.mainClass="net.ars.io.Main"`  
  
  
## Compile and Run - Receiver  
  
`mvn clean compile`  
  
`mvn exec:java -Dexec.mainClass="net.ars.server.App" -Dexec.args="<path_to>/akka-actor-io-receiver.yml"`  
  
  
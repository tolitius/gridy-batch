## What is "Gridy Batch"? ##

A reference application that demonstrates:

+ sharing offline transactions via distributed cache ( Hazelcast )

+ using Spring Batch in a non-traditional way ( staging test data )

+ Spring Batch partitioning / commit interval

+ GridGain zero deploy / fail over

+ GridGain ScalaR

+ using Scala and Java within the same problem domain / project

+ see? there is no need for "spring-scala" project, "scala spring bean" already works :)

+ Hazelcast transactions to play along with Spring Batch commit interval

+ partitioning over Hazelcast by submitting distributed tasks via java.util.concurrent.ExecutorService

+ Gradle

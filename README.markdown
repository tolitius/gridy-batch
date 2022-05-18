## what is "gridy batch"? ##

A reference application that demonstrates:

+ sharing offline transactions via distributed cache ( Hazelcast )

+ using Spring Batch in a non-traditional way ( staging test data )

+ Spring Batch partitioning / commit interval

+ GridGain zero deployment / fail over

+ GridGain ScalaR

+ using Scala and Java within the same problem domain / project

+ see? there is no need for "spring-scala" project, "scala spring bean" already works :)

+ Hazelcast transactions to play along with Spring Batch commit interval

+ partitioning over Hazelcast by submitting distributed tasks via java.util.concurrent.ExecutorService

+ Gradle


## what's with the name?

batch jobs on a grid.. but<br>
as [@danielmiladinov](https://github.com/danielmiladinov) pointed out, it does pay respect to [Grady Booch](https://en.wikipedia.org/wiki/Grady_Booch)

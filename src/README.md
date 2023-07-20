## Pipelines
This subfolder contains two pipelines: BasicPipeline and TDSPipeline (first run WriteToPostgreSql). 

The first reads from a PostgreSQL database created in pgadmin, then it writes to a hive table.

The second looks at Minneapolis traffic stop data to identify a racial bias in policing. Though not altogether in one scala class, this is really a pipeline taking local csv data, sending it to a database, then retrieving it for Spark analysis.

## Scala Functional
Comprehensive display of various advanced functional programming concepts in scala.

## Scala OOP
Comprehensive display of various object oriented programming concepts in scala.

## Superheroes
Demo analysis of graph data using Spark, with an unfinished GraphX demo that I hope to return to.

## MovieLens
DataFrame and DataSet practice using movielens data (movies.csv, not to be confused with ../data/movies.json)

## DataFrames
Comprehensive demo and practice with various DataFrame concepts

## TypesAndDatasets
Short demo and practice with DataSets and Spark types, including handling nulls and working with RDDs.

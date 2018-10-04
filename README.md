# Big Data Analytics Cloud Platform

A scalable platform to perform distributed linguistic analyses on geolocalized 
tweets using [Apache Hadoop](https://hadoop.apache.org/) on top of the 
[Google Cloud Platform](https://cloud.google.com/).
This software is linked to the 
[Mappa dell’Intolleranza](http://www.voxdiritti.it/vox-lancia-la-prima-mappa-dellintolleranza/), 
an Italian project designed to detect and prevent phenomena related to 
homophobia, racism, misogyny and other forms of intolerance.

Please refer to the 
[thesis](https://github.com/ShadowTemplate/big-data-analytics-cloud-platform/blob/master/Tesi%20Taneburgo%20Gianvito%20-%20Piattaforma%20cloud%20per%20l'analisi%20di%20big%20data%20provenienti%20da%20social%20network.pdf) 
for additional details on the project goal, structure and results.

---
## Information

**Status**: `Completed`

**Type**: `Academic project`

**Course**: `Bachelor's degree`

**Development year(s)**: `2014`

**Author(s)**: Pasquale Lops,
[pippokill](https://github.com/pippokill), 
[ShadowTemplate](https://github.com/ShadowTemplate)

**Notes**: The *Mappa dell’Intolleranza* project is being actively developed by 
a group of universities (Università degli Studi di Bari "Aldo Moro", Università 
degli Studi di Roma "La Sapienza", Università Cattolica del Sacro Cuore di 
Milano).
You can have a look at the generated hate maps by reading the yearly reports.
At the time of writing [Vox](http://www.voxdiritti.it/) has published results 
for the 
[first](http://www.voxdiritti.it/ecco-le-mappe-di-vox-contro-lintolleranza/), 
[second](http://www.voxdiritti.it/ecco-la-nuova-edizione-della-mappa-dellintolleranza/) 
and 
[third](http://www.voxdiritti.it/la-mappa-dellintolleranza-anno-3-la-nuova-radiografia-dellitalia-che-odia-online/) 
year.

---
## Getting Started

The provided code was used to perform a wide range of tasks during my thesis 
work, such as crawling tweets, processing data sets, indexing and performing 
linguistic analyses in a distributed fashion.

Due to the nature of the technologies involved, reproducing all the steps and 
the pipeline is unfortunately not straightforward.
For instance, MapReduce jobs were run on [Hadoop](https://hadoop.apache.org/) 
clusters deployed on [Google Compute Engine](https://cloud.google.com/compute/)
that requires an account with billing enabled.

In addition, even sharing input/output files is complicated due to the large 
size of the data set involved.
In fact, the linguistic analyses have involed **itWaC**, a 2 billion word 
corpus (6+ GB) constructed from the Web using medium-frequency words from a 
newspaper and basic Italian vocabulary lists as seeds.
The data set can be downloaded 
[here](http://wacky.sslmit.unibo.it/doku.php?id=download).

Whenever possible all the most important files have been uploaded to this repo.
You can find the most relevant ones in the [input](src/input) and 
[output](src/output) folders.

The [pom.xml](src/pom.xml) file should provide sufficient information on all 
the required dependencies.
However, at the time of writing some of them seem to be already deprecated, 
such as the library required to crawl tweets in real time.

A good starting point for the curious reader is the 
[task package](src/src/java/it/uniba/di/bdacp/task).
It contains the main entry points for the application and all the facade 
classes to be used to run the different tasks.
Each task is documented and provides details on how it should be invoked.
In addition, some commands can be found in 
[these examples](https://github.com/ShadowTemplate/big-data-analytics-cloud-platform/blob/master/command%20examples.txt). 

The most important MapReduce task is a distributed computation of the 
[Kullback–Leibler divergence](https://en.wikipedia.org/wiki/Kullback%E2%80%93Leibler_divergence)
and it is implemented in the [KLDivergenceJob class](src/src/java/it/uniba/di/bdacp/tools/KLDivergenceJob.java).
However, the reader is recommended to first read its formal and clean 
description in the 
[thesis](https://github.com/ShadowTemplate/big-data-analytics-cloud-platform/blob/master/Tesi%20Taneburgo%20Gianvito%20-%20Piattaforma%20cloud%20per%20l'analisi%20di%20big%20data%20provenienti%20da%20social%20network.pdf).

A quick description of the collected results can be found in the 
[presentation](https://github.com/ShadowTemplate/big-data-analytics-cloud-platform/blob/master/Presentazione%20Tesi%20Taneburgo%20Gianvito%20-%20Piattaforma%20cloud%20per%20l'analisi%20di%20big%20data%20provenienti%20da%20social%20network.pdf)
used to discuss my thesis.
An [extended version](https://github.com/ShadowTemplate/big-data-analytics-cloud-platform/blob/master/Presentazione%20Tesi%20Taneburgo%20Gianvito%20-%20Studenti%20Magistrale.pdf) 
for a demo to Master's students is also provided.

---
## Building tools

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html) - 
Programming language
* [Maven](https://maven.apache.org/) - Build automation
* [Apache Hadoop](https://hadoop.apache.org/) - MapReduce framework
* [Apache Lucene](https://lucene.apache.org/) - NLP pipeline
* [Tweet NLP](https://www.cs.cmu.edu/~ark/TweetNLP/) - Tweets processing
* [Google Compute Engine](https://cloud.google.com/compute/) - Hadoop cluster
* [Google Cloud Storage](https://cloud.google.com/storage/) - File storage
* [Hosebird](https://github.com/twitter/hbc) - Java HTTP client for Twitter 
Streaming API
* [Jackson](https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl) - JSON Object Mapper
* [SLF4J](https://www.slf4j.org/) - Logging

---
## Contributing

This project is not actively maintained and issues or pull requests may be 
ignored.

---
## License

This project is licensed under the GNU GPLv3 license.
Please refer to the [LICENSE.md](LICENSE.md) file for details.

---
*This README.md complies with [this project template](
https://github.com/ShadowTemplate/project-template). Feel free to adopt it
and reuse it.*

:title Hazelcast queues are not partitioned
:published 2014-05-01
:dek Weak documentation
:body

There. I said it, just in case you were looking for answers in the [documentation](http://www.hazelcast.org/docs/latest/manual/html/queue.html)
and was coming up empty handed.

First of all, I like Hazelcast; it has a simple API, can be embedded in your application and works out of the box when
you need to distributed data structures across processes or machines.

So how do the queues work in Hazelcast?

Based on the documentation on their [forum](https://groups.google.com/forum/#!searchin/hazelcast/conceptual$20overview$20of$20how$20queues/hazelcast/Gvq2TTAaWrE/eogDIYadf2EJ), I think the following applies:

* A queue exists only on a single node in a cluster
* A queue may have backups on other nodes
* The client code is oblivious to which node owns the queue
* There is no guarantee of the processing order of messages on the queue
* Since the queue exists on a single node in its entirety, you can't increase the maximum size of the queue by scaling horizontally
* Messages can only be consumed one by one in a transaction.
* If you want to consume messages in bulk, you can choose between [bad performance or potentially loosing messages](https://groups.google.com/d/msg/hazelcast/Gvq2TTAaWrE/RJBvR8jK1XAJ)
* A queue can be persisted if you implement the persistence yourself, typically to a [central repository](https://groups.google.com/forum/#!topic/hazelcast/Wa6gELKB3fk).

Here is an example of a cluster with a single queue and a backup

![Hazelcast queue](/images/hazelcast_queue.png)

A trick you can do to scale the size of a queue is to stripe it, i.e create several queues that represents a single queue. Then you write to each of the queues in a round-robin style, and poll from all the queues. 
Obviously not a very scalable solution.

Stuff I'm still uncertain about:

* How does Hazelcast decide which node is responsible for a given queue?
* How is the performance affected when the number of reads increases?
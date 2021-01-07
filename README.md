[![Build Actions Status](https://github.com/backstreetkiwi/lexi4j/workflows/mavenbuild/badge.svg)](https://github.com/backstreetkiwi/lexi4j/actions)

# lexi4j
Wrapping Linux EXIF tools in Java

## Motivation
In a small project to archive digital images, I used [Apache Commons Imaging][commons-imaging] which worked quite well. Eventually I decided to switch to use Linux command line tools wrapped in Java. The reasons for this decision:

* **`commons-imaging`** supports many different file types and metadata formats. Due to this high level of abstraction, the usage seems a little cumbersome for me.
* **`commons-imaging`** still is pre version 1 and there is not much activity. No offence, I know maintaining Open Source stuff in your spare time can be a hard job, but it seems to me that not many people are using the software.
* I once had a major f***-up in my digital image archive (I was able to restore it thanks to a good backup strategy). I guess it was rather my fault than the library's, but it was really hard to figure out what went wrong. Long story short, it had to do with encoding...

As I am used to smart little Linux tools like **`exiftool`** I decided to switch to using them. As I wanted to have some caching, I came up with the idea of wrapping it in a Java lib. Let's go...

[commons-imaging]: https://commons.apache.org/proper/commons-imaging/

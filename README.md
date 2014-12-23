ProjectNova
===========

Team project Nova

To Run the file:

1. Crawl the webpages
1.1 cd main
1.2 python get_rss.py
2. Recompile the jar file/Export project onto eclipse and run
2.1 cd -
2.2 java -jar main.jar
3. Web UI
  3.1 ssh ubuntu@ec2-54-149-127-195.us-west-2.compute.amazonaws.com
  3.2 cd ProjectNova
  3.3 sudo PORT=80 npm start
  3.4 Go to http://54.149.127.195/?page=1 to view the results

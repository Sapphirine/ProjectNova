import urllib2
import urllib
import requests
from bs4 import BeautifulSoup
import re
import os.path
import os

page = urllib2.urlopen("http://www.cnn.com")
soup = BeautifulSoup(page)

page2 = urllib2.urlopen("http://bbcworld.com")
soup2 = BeautifulSoup(page2)

#Finding the RSS feeds
link1 = soup.find('link',type='application/rss+xml')
link2 = soup2.find('link',type='application/rss+xml')


# soup = BeautifulSoup(open(link1['href']));
soup= BeautifulSoup(urllib2.urlopen(link1['href'])); #CNN link
soup2 = BeautifulSoup(urllib2.urlopen("http://feeds.bbci.co.uk/news/rss.xml?edition=us"));

temp_dict1={};
temp_dict2={};

#For BBC Clustering of RSS
dict1 = {};
title = soup2.find_all('title');
guid_link = soup2.find_all('guid');
description = soup2.find_all('description')

# num=0;

num=0
k=1


str3="file_input/BBC_Index_FileName_Matching.txt"
str4="file_input/BBC_Link_Matching.txt"
if os.path.exists(str3):
	file = open(str3,'r+')
	file.truncate();
	file.close();

if os.path.exists(str4):
	file = open(str4,'r+')
	file.truncate();
	file.close();

for i in title:
		if num==0 or num==1:
			num=num+1
			continue
	# if i.string not in dict1.keys():
		else:
			try:
				
				s = BeautifulSoup(urllib2.urlopen(guid_link[num-2].string))

				title_name = str(i.string);
				if "VIDEO:" in title_name:
						num=num+1
						continue

				str2 = "none"
				p_all = s.find_all('p')
				str1 = "rawInput/"+str(num-2)+".txt"
		
				file = open(str1,'w')
			
				for p in p_all:

					line = str(p.string)
					line = line.replace('"',' ');
					line = line.replace('\'',' ');
					str2 = str2 + line +"\n"

				file.write(str2);
				file.flush();
				file.close();

				file= open(str3,'a')
				title_name = str(i.string);
				print title_name
				title_name = title_name.replace('"',' ');
				title_name = title_name.replace('\'',' ');
				file.write(str(num-2)+"\t"+title_name+"\n")
				file.flush();
				file.close();

				file= open(str4,'a')
				link_name = str(guid_link[num-2].string);
				print link_name
				file.write(str(num-2)+"\t"+link_name+"\n")
				file.flush();
				file.close();

				num=num+1
				
			except Exception:
				num=num+1
				continue


# # #For NYT clustering of RSS
# dict2={};
title = soup.find_all('title');
guid_link = soup.find_all('guid');
description = soup.find_all('description')
num=0;

str3="file_input/NYT_Index_FileName_Matching.txt"
if os.path.exists(str3):
	file = open(str3,'r+')
	file.truncate();
	file.close();

str4="file_input/NYT_Link_Matching.txt"
if os.path.exists(str4):
	file = open(str4,'r+')
	file.truncate();
	file.close();

for i in title:
		
			if num==0 or num==1:
				num=num+1
				continue
			else:
				try:
					title_name=str(i.string)
					if "VIDEO:" in title_name:
						num=num+1
						continue
					
					str2 = "none"
					# str1 = "/Users/hadoop/Documents/Project_Nova/main/rawInput/2_"+str(num-2)+".txt"
					str1 = "rawInput/2_"+str(num-2)+".txt"

					s = BeautifulSoup(urllib2.urlopen(guid_link[num-2].string))
					p_all = s.findAll("p")
					file = open(str1,'w')
					
					for p in p_all:
						line = str(p.string)
						line = line.replace('"',' ');
						line = line.replace('\'',' ');
						str2 = str2 + line+"\n"
							
					file.write(str2);
					file.flush();
					file.close();

					file= open(str3,'a')
					title_name = title_name.replace('"',' ');
					title_name = title_name.replace('\'',' ');
					file.write("2_"+str(num-2)+"\t"+title_name+"\n")
					file.flush();
					file.close();

					file= open(str4,'a')
					link_name = str(guid_link[num-2].string);
					file.write("2_"+str(num-2)+"\t"+link_name+"\n")
					file.flush();
					file.close();		
						
					num=num+1
				

				
				except Exception:
					num=num+1
					continue

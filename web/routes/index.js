var express = require('express');
var router = express.Router();

/* GET home page. */

/* GET Userlist page. */
/*router.get('/', function(req, res) {
    var db = req.db;
    var collection = db.get('comment');
    var p=req.query.page;
    console.log(p);
    collection.find({page:Number(p)},function(e,docs){
        res.render('index', {
            "userlist" : docs
        });
        //console.log(docs);
    });
});

/* GET New User page. */

router.get('/', function(req, res) {
    var p=Number(req.query.page);

    req.getConnection(function(err,connection){
        connection.query('SELECT clusternum  FROM Cluster',function(err,clusternum){
            var cluster=new Array();
            var len=0;
            if(err)
                console.log("Error Selecting : %s ",err );
            for(var key in clusternum){
                var flag=0;
                for(var j in cluster){
                    if(cluster[j]==clusternum[key].clusternum) {flag=1;break;}
                }
                if(flag==0){
                    cluster[++len]=clusternum[key].clusternum;
                }
            }
            console.log(cluster[1]);



       // var cluster=[0,7,2,11,10];
        connection.query('SELECT a.title,a.content,a.link FROM Cluster c, Article a where c.num=a.num and c.clusternum='+(cluster[Number(p)]),function(err,rows)     {

            if(err)
                console.log("Error Selecting : %s ",err );
            //console.log(p);
            res.render('index2',{list:rows,p:Number(p)});


        });
        });
    });


});







/* POST to Add User Service */


module.exports = router;

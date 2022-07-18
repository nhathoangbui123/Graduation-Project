const AWS = require("aws-sdk")
const documentClient = new AWS.DynamoDB.DocumentClient({
  region: "us-east-2",
})

const express = require('express')
const bodyParser = require('body-parser')
const awsServerlessExpressMiddleware = require('aws-serverless-express/middleware')
const app = express()

const iotData = new AWS.IotData({ endpoint: "a3suuuxay09k3c-ats.iot.us-east-2.amazonaws.com" });

app.use(bodyParser.json())
app.use(awsServerlessExpressMiddleware.eventContext())

//publish to iot core
async function sendIot(req) {
    var params = {
      payload:JSON.stringify(req.body),
      topic: "ESP32/sub",
      qos: 0 
    };

    //console.log("params: " + JSON.stringify(params));

    try {
        let iotPromise = await iotData.publish(params).promise();
        //console.log('AWS IoT: resp: ' + JSON.stringify(iotPromise))
        return iotPromise;
    } catch (e) {
      //console.log("Error: " + e);
    }
  }
// Enable CORS for all methods
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*")
  res.header("Access-Control-Allow-Headers", "*")
  next()
});


//get
app.get('/todo', function(req, res) {
  //console.log('url: ', req.url);
  documentClient
  .get({
    TableName: "ESP32",
    Key: {
      MAC: "01:02:03:04:05:06",
    },
  })
  .promise()
  .then(data => {
    //console.log(JSON.stringify(data.Item))
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(data.Item));
  })
  .catch(console.error)
  
  //console.log('body get: ', res.json);
});

//get user
app.get('/todo/user', function(req, res) {
  const cognitoidentityserviceprovider = new AWS.CognitoIdentityServiceProvider()
  var params = {
    UserPoolId: "us-east-2_T5jtiXIkb", 
    Limit: '10',
  };
  cognitoidentityserviceprovider.listUsers(params, function(err, data) {
    if (err) {
      return res.status(500).json(err)
    }
    res.json({users: data.Users});
  });
});
app.get('/todo/*', function(req, res) {
  res.json({success: 'get call succeed!', url: req.url});
});


//post
app.post('/todo', function(req, res) {
  //console.log('body post: ', req.body);
  
  // let {Sdev1, Sdev2, Sdev3, Sdev4, Sdeva} = req.body;
  
  // sendIot(req);
  
  // console.log({Sdev1, Sdev2, Sdev3, Sdev4, Sdeva});
  
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #Sdev1 = :_Sdev1, #Sdev2 = :_Sdev2, #Sdev3 = :_Sdev3, #Sdev4 = :_Sdev4',
  //   ExpressionAttributeNames: {
  //       '#Sdev1': 'Sdev1',
  //       '#Sdev2': 'Sdev2',
  //       '#Sdev3': 'Sdev3',
  //       '#Sdev4': 'Sdev4'
  //   },
  //   ExpressionAttributeValues: {
  //     ":_Sdev1": Sdev1,
  //     ":_Sdev2": Sdev2,
  //     ":_Sdev3": Sdev3,
  //     ":_Sdev4": Sdev4,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Status Dev 1
app.post('/todo/Sdev1', function(req, res) {
  //console.log('body post: ', req.body);
  let {Sdev1} = req.body;
  sendIot(req);
  //console.log({Sdev1});
  documentClient
  .update({
    TableName: "ESP32",
    Key: {
      MAC: "01:02:03:04:05:06",
    },
    UpdateExpression: 'set #Sdev1 = :_Sdev1',
    ExpressionAttributeNames: {
        '#Sdev1': 'Sdev1',
    },
    ExpressionAttributeValues: {
      ":_Sdev1": Sdev1,
    },
  })
  .promise()
  .then(data => console.log(data.Attributes))
  .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Status Dev 2
app.post('/todo/Sdev2', function(req, res) {
  //console.log('body post: ', req.body);
  let {Sdev2} = req.body;
  sendIot(req);
  //console.log({Sdev2});
  documentClient
  .update({
    TableName: "ESP32",
    Key: {
      MAC: "01:02:03:04:05:06",
    },
    UpdateExpression: 'set #Sdev2 = :_Sdev2',
    ExpressionAttributeNames: {
        '#Sdev2': 'Sdev2',
    },
    ExpressionAttributeValues: {
      ":_Sdev2": Sdev2,
    },
  })
  .promise()
  .then(data => console.log(data.Attributes))
  .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Status Dev 3
app.post('/todo/Sdev3', function(req, res) {
  //console.log('body post: ', req.body);
  let {Sdev3} = req.body;
  sendIot(req);
  //console.log({Sdev3});
  documentClient
  .update({
    TableName: "ESP32",
    Key: {
      MAC: "01:02:03:04:05:06",
    },
    UpdateExpression: 'set #Sdev3 = :_Sdev3',
    ExpressionAttributeNames: {
        '#Sdev3': 'Sdev3',
    },
    ExpressionAttributeValues: {
      ":_Sdev3": Sdev3,
    },
  })
  .promise()
  .then(data => console.log(data.Attributes))
  .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Status Dev 4
app.post('/todo/Sdev4', function(req, res) {
  //console.log('body post: ', req.body);
  let {Sdev4} = req.body;
  sendIot(req);
  //console.log({Sdev4});
  documentClient
  .update({
    TableName: "ESP32",
    Key: {
      MAC: "01:02:03:04:05:06",
    },
    UpdateExpression: 'set #Sdev4 = :_Sdev4',
    ExpressionAttributeNames: {
        '#Sdev4': 'Sdev4',
    },
    ExpressionAttributeValues: {
      ":_Sdev4": Sdev4,
    },
  })
  .promise()
  .then(data => console.log(data.Attributes))
  .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Electricity Price
app.post('/todo/EP', function(req, res) {
  //console.log('body post: ', req.body);
  let {EP} = req.body;
  sendIot(req);
  //console.log({EP});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Wifi Name 
app.post('/todo/WN', function(req, res) {
  //console.log('body post: ', req.body);
  let {WN} = req.body;
  sendIot(req);
  //console.log({WN});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Wifi Pass  
app.post('/todo/WP', function(req, res) {
  //console.log('body post: ', req.body);
  let {WP} = req.body;
  sendIot(req);
  //console.log({WP});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Device 1 Threshold  
app.post('/todo/T1', function(req, res) {
  //console.log('body post: ', req.body);
  let {T1} = req.body;
  sendIot(req);
  //console.log({T1});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Device 2 Threshold  
app.post('/todo/T2', function(req, res) {
  //console.log('body post: ', req.body);
  let {T2} = req.body;
  sendIot(req);
  //console.log({T2});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Device 3 Threshold  
app.post('/todo/T3', function(req, res) {
  //console.log('body post: ', req.body);
  let {T3} = req.body;
  sendIot(req);
  //console.log({T3});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//enpoint Device 4 Threshold  
app.post('/todo/T4', function(req, res) {
  //console.log('body post: ', req.body);
  let {T4} = req.body;
  sendIot(req);
  //console.log({T4});
  // documentClient
  // .update({
  //   TableName: "ESP32",
  //   Key: {
  //     MAC: "01:02:03:04:05:06",
  //   },
  //   UpdateExpression: 'set #EP = :_EP',
  //   ExpressionAttributeNames: {
  //       '#EP': 'EP',
  //   },
  //   ExpressionAttributeValues: {
  //     ":_EP": EP,
  //   },
  // })
  // .promise()
  // .then(data => console.log(data.Attributes))
  // .catch(console.error)
  res.json({success: 'post call succeed!', url: req.url, body: req.body})
});
//put
app.put('/todo', function(req, res) {
  res.json({success: 'put call succeed!', url: req.url, body: req.body})
});

app.put('/todo/*', function(req, res) {
  res.json({success: 'put call succeed!', url: req.url, body: req.body})
});



//delete
app.delete('/todo', function(req, res) {
  res.json({success: 'delete call succeed!', url: req.url});
});

app.delete('/todo/*', function(req, res) {
  res.json({success: 'delete call succeed!', url: req.url});
});



//listen
app.listen(3000, function() {
    console.log("App started")
});

module.exports = app


console.log('May Node be with you')
const express = require('express')
const bodyParser= require('body-parser')
const app = express();
const MongoClient = require('mongodb').MongoClient
const connectionstring = 'mongodb+srv://Interview:Interview@interview.7lxtm.mongodb.net/InterviewDB?retryWrites=true&w=majority'
MongoClient.connect( connectionstring,{useUnifiedTopology: true}, (err, client) => {
  if (err) return console.error(err)
  console.log('Connected to Database')
  const db = client.db('InterviewDB')
  const interviewCollection = db.collection('patients')
  app.set('view engine', 'ejs')
  app.use(bodyParser.urlencoded({ extended: true }))
  app.listen(3000, function() {
    console.log('listening on 3000')
  })
  app.get('/', (req, res) => {
    //res.sendFile(__dirname + '/index.html')
    db.collection('patients').find().toArray()
    .then(results => {
      //console.log(results)
      //Render Added
      res.render('index.ejs', { patients: results })
    })
    .catch(error => console.error(error))
   
  })
  app.post('/appointment', (req, res) => {
    interviewCollection.insertOne(req.body)
      .then(result => {
        console.log(result)
        res.redirect('/')
      })
      .catch(error => console.error(error))
  })
})
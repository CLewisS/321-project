
module.exports = function(req, res, next) {
  console.log('In chat server');
  //res.send('<p> Chat Server </p>');
  next();
};

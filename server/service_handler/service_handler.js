/* This module handles service requests
 *
 */

module.exports = function (req, res, next) {
  console.log('In service handler');
  //res.send('<p> Service Handler </p>');  

  next();
}

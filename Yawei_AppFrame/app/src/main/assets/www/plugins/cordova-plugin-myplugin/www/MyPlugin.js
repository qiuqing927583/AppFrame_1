cordova.define("cordova-plugin-myplugin.MyPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (info, coolMethod,success, error) {
    exec(success, error, 'ionicMyPlugin', coolMethod, [info]);
};
// var MyPlugin = (function () {
//     function MyPlugin()
//     {
        
//     }
//     MyPlugin.prototype.invokeNativeMethod = function (success, error, methodName, args)
//     {
//         cordova.exec(success, error, "MyPlugin", methodName, args);
//     };
// }());

// var myplugin = {}

// myplugin.coolMethod = function(info,success, error) {
//     exec(success, error, "MyPlugin", "coolMethod", [info]);
// };
// // //求和方法
// // myplugin.plus = function(arg0,arg1, success, error) {
// //     exec(success, error, "MyPlugin", "plus", [arg0,arg1]);
// // };

// module.exports = myplugin;

});

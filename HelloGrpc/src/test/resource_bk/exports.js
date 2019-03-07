var HelloWorldProto = require('./helloworld_pb');
var HelloServiceProto = require('./helloworld_grpc_web_pb');

module.exports = {
    DataProto: HelloWorldProto,
    CommunicationProto: HelloServiceProto
}
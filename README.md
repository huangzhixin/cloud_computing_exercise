# cloud_computing_exercise

=======================================ccexercise_1============================================

http://134.169.47.184:4222/juddi

http://134.169.47.184:4223/MWFacebookService?wsdl

sudo wsimport -p mw.facebookclient -d bin -s src -keep http://134.169.47.184:4223/MWFacebookService?wsdl

sudo wsimport -p mw.pathclient -d bin -s src -keep http://localhost:12345/MWPathSrv?wsdl
sudo chmod 777 ./ -R

java -cp ./:jaxr-api.jar:jarx-impl-mwcc.jar mw/MWRegistryAccess LIST MWFacebookServiced

Nicolas Van den Abeele
Alan Fish Williams
Seth Raul Vettel ❼
Ole-Kjetil Gjelstad
Penya Motera del Valles
Ireneusz Kuzdub
Arlradio Fmsimphony
Míra Masár
Samy Kartit

================================================================================================

=========================================ccexercise_3===========================================
cd ..../bin

java -cp ./:jaxr-api.jar:jarx-impl-mwcc.jar /mw/path/MWPathPubishSrv

java -cp ./:jaxr-api.jar:jarx-impl-mwcc.jar /mw/cache/MWCache

java -jar jetty-runner-9.0.0.M4.jar hello/out/artifacts/hello_war_exploded


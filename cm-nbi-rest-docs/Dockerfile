FROM bbrodriges/raml2html

VOLUME ./src/resources/api:/app
ENV INPUT=cm-nbi-rest-api.raml
ENV OUTPUT=cm-nbi-rest-api.html
RUN bbrodriges/raml2html
#RUN --rm -v /api:/app -e "INPUT=cm-nbi-rest-api.raml" -e "OUTPUT=cm-nbi-rest-api.html" bbrodriges/raml2html
#RUN /home/enmadmin/NBI/cm-nbi-rest/docker/swagger-ui/api:/app -e "INPUT=cm-nbi-rest-api.raml" -e "OUTPUT=cm-nbi-rest-api.html" bbrodriges/raml2html
#CMD /home/enmadmin/NBI/cm-nbi-rest/docker/swagger-ui/api:/app -e "INPUT=cm-nbi-rest-api.raml" -e "OUTPUT=cm-nbi-rest-api.html" bbrodriges/raml2html

#CMD redoc-cli bundle /api/cm-nbi-rest-api.raml -o cm-nbi-rest-api.html

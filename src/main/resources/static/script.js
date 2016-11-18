document.addEventListener('DOMContentLoaded', function () { // on dom ready

        var cy = cytoscape({
            container: document.querySelector('#cy'),

            boxSelectionEnabled: false,
            autounselectify: true,

            style: cytoscape.stylesheet()
                .selector('node')
                .css({
                    'content': 'data(name)',
                    'text-valign': 'center',
                    'color': 'white',
                    'text-outline-width': 2,
                    'background-color': '#999',
                    'text-outline-color': '#999'
                })
                .selector('edge')
                .css({
                    'curve-style': 'bezier',
                    'target-arrow-shape': 'triangle',
                    'target-arrow-color': '#ccc',
                    'line-color': '#ccc',
                    'width': 1
                })
                .selector(':selected')
                .css({
                    'background-color': 'black',
                    'line-color': 'black',
                    'target-arrow-color': 'black',
                    'source-arrow-color': 'black'
                })
                .selector('.faded')
                .css({
                    'opacity': 0.25,
                    'text-opacity': 0
                }),

            layout: {
                name: 'grid',
                padding: 10
            }
        });

        cy.on('tap', 'node', function (e) {
            var node = e.cyTarget;
            var neighborhood = node.neighborhood().add(node);

            cy.elements().addClass('faded');
            neighborhood.removeClass('faded');
        });

        cy.on('tap', function (e) {
            if (e.cyTarget === cy) {
                cy.elements().removeClass('faded');
            }
        });

        // window.setInterval(function () {
        //
        //
        //
        //
        //
        // }, 5000);


        var slider = $("#ex5").slider({});


        range();
        elements();


        function buildUri(contextPath, to) {
            var uri;
            if (to !== undefined) {
                uri = "/" + contextPath + "?to=" + to.valueOf();
            } else {
                uri = "/" + contextPath + "?to=" + new Date().valueOf();
            }
            return uri;
        }

        function elements(to) {
            var uri = buildUri("elements", to);
            $.get(uri, function (data) {
                cy.elements().remove();
                cy.add(data);

                callDurations(to, function (data) {
                    cy.edges().forEach(function (n) {
                        n.qtip({
                            content: {
                                text: function (event, api) {
                                    var data = n.data();
                                    var content = "";
                                    for (var k in data) {
                                        if (data.hasOwnProperty(k)) {
                                            content += k + "=" + data[k] + "<br/>";
                                        }
                                    }
                                    return content;
                                }
                            }
                        });
                        cy.layout({name: 'circle'});
                    });
                });


            });
        }

        function range() {
            $.get("/range", function (data) {
                var from = moment(new Date(data.from));
                var to = moment(new Date(data.to));
                var diffInHours = to.diff(from, 'minutes');

                var mySlider = $("#ex5").slider({
                    min: 0,
                    max: diffInHours,
                    value: diffInHours,
                    formatter: function (value) {
                        return moment(new Date(data.from)).add(value, 'm')
                    }
                });

                mySlider.on('slideStop', function (slideEvt) {
                    var selectedTo = moment(new Date(data.from)).add(slideEvt.value, 'm').toDate();
                    elements(selectedTo);
                });
            });
        }

        function callDurations(to, callback) {
            var uri = buildUri("callDuration", to);
            $.get(uri, function (data) {
                data.forEach(function (value) {
                    if (value.edge !== undefined) {
                        var edges = cy.edges('[source = "' + value.edge.source + '"][target = "' + value.edge.target + '"]')
                        if (edges !== undefined) {

                            var data = value.edgeData;
                            for (var k in data) {
                                if (data.hasOwnProperty(k)) {
                                    edges[0].data(k, data[k]);
                                }
                            }
                            callback(edges[0]);
                        }
                    }
                });
            });
        }


    }
); // on dom ready


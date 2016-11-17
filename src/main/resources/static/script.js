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

    function elements(to) {
        var uri;
        if (to !== undefined) {
            uri = "/elements?to=" + to.valueOf();
        } else {
            uri = "/elements?to=" + new Date().valueOf();
        }
        $.get(uri, function (data) {
            cy.elements().remove();
            cy.add(data);
            cy.layout({name: 'circle'});
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

}); // on dom ready


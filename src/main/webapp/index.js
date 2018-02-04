var zi = {};
for (var x = 0; x < 15; x++) {
    var row = {};
    for (var y = 0; y < 15; y++) {
        row[y] = 0;
    }
    zi[x] = row;
}

var app = new Vue({
    el: '#app',
    data: function () {
        return {
            zi: zi,
        }            
    },
    methods: {
        put(x, y) {
            if (zi[x][y] == 0) {
                Vue.http.post("/gobang/play", {x:x, y:y}).then(function(data){
                    if(data.ok){
                        var color =  JSON.parse(data.bodyText);
                        for (var x = 0; x < 15; x++) {
                            for (var y = 0; y < 15; y++) {
                                zi[x][y] = color[x][y];
                            }
                        }
                    }
                });
            }
        },
        color(x, y) {
            var v = zi[x][y];
            var c = v == 0 ? '' : v == 1 ? 'cell white' : 'cell black';
            return c;
        }
    },
})


Vue.http.post("/gobang/play", null).then(function(data){
    if(data.ok){
        var color =  JSON.parse(data.bodyText);
        for (var x = 0; x < 15; x++) {
            for (var y = 0; y < 15; y++) {
                zi[x][y] = color[x][y];
            }
        }
    }
});

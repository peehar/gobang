var zi = {};
for (var y = 0; y < 15; y++) {
    var row = {};
    for (var x = 0; x < 15; x++) {
        row[x] = 0;
    }
    zi[y] = row;
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
            zi[y][x] = zi[y][x] == 0 ? 1 : zi[y][x] == 1 ? 2 : 0;
        },
        color(x, y) {
            var v = zi[y][x];
            var c = v == 0 ? '' : v == 1 ? 'cell white' : 'cell black';
            return c;
        }
    },
})

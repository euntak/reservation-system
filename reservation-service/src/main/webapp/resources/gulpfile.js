var gulp = require('gulp');
var webserver = require('gulp-webserver');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var minify = require('gulp-minify');

var css = require('gulp-css');
var minifyhtml = require('gulp-minify-html');
var livereload = require('gulp-livereload');

var src = 'resources';
var dist = '../dist';

var paths = {
    js : src + '/js/**/*.js',
    css : src + '/css/*.css'
}
gulp.task('server',function(){
    return gulp.src(dist + '/')
        .pipe(webserver());
});

gulp.task('init',function(){
    return gulp.src('resources/lib/*.js')
        .pipe(uglify())
        .pipe(concat('mylib.js'))
        .pipe(gulp.dest(dist+'/js'));
});

gulp.task('combine-js',['init'],function(){
    return gulp.src(paths.js)
        .pipe(uglify())
        .pipe(minify({
            ext:{
                src:'-debug.js',
                min:'.js'
            },

        }))
        .pipe(gulp.dest(dist+'/js'));//여기로 보낸다
});

gulp.task('compile-css',function(){
    return gulp.src(paths.css)
    .pipe(css()).pipe(gulp.dest(dist+'/css'));
});

gulp.task('watch',function(){
    livereload.listen();
    gulp.watch(paths.js,['combine-js']);
    gulp.watch(paths.css,['compile-css']);
    gulp.watch(dist+'/**').on('change',livereload.changed);
});

gulp.task('default',['server','combine-js','compile-css','watch']);


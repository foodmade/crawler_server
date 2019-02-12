var _VER = '1.0.0';
var _INDEX = 0;
var _TYPE = 'Baishi:search_home_list';

var _RETCODE_SUCCESS = 1;
var _RETCODE_ERROR = -1;


function log(log, level){
    utils.log('{DParser Baishi:search_home_list ' + _INDEX + '(ver:' + _VER + ')} ' + log, level);
}

function parserHtml() {
    var result = {
        rows :[],
        retcode:_RETCODE_ERROR
    };
    var $ = cheerio.load(htmlString);
    var videoList = $('ul#contents li');
    if(videoList.length === 0){
        return result;
    }
    for (var i = 0;i<videoList.length;i++){
        let node = $(videoList[i]);
        /**封面图*/
        var coverImg = node.find('img').attr('src');
        /**是否高清**/
        var videoType = node.find('i.box-img-txt').text();
        /**电影名称**/
        var videoName = node.find('a').attr('title');
        /**人物信息**/
        var figures = node.find('p.box-b2-t').text();
        /**视频 id**/
        var videoId = node.find('a').attr('href');
        let ids = videoId.split('/');
        videoId = ids[ids.length-2];
        /***评分*/
        var score = node.find('span.box-b2-score').text();
        score = score.replace('分','');
        log('coverImg:'+coverImg,4);
        log('videoType:'+videoType,4);
        log('videoName:'+videoName,4);
        log('score:'+score,4);
        log('figures:'+JSON.stringify(figures),4);
        log('videoId:'+videoId,4);
        result.rows.push({
            coverimg:coverImg,
            videotype:videoType,
            videoname:videoName,
            figures:figures,
            videoid:videoId,
            score:score
        })
    }
    log('22222222222222222222222222',4);
    result.retcode = _RETCODE_SUCCESS;
    return result;

}

function parserAllFigure(actors, $) {
    var result = {
        actor:[],
        director:[]
    };

    if(actors === undefined || actors.length === 0){
        return result;
    }

    for (var j = 0; j < actors.length;j++) {
        var figureDetail = $(actors[j]).find('a');
        if(j === 0){
            /**演员**/
            result.actor = parserFigure(figureDetail,$);
        }else if(j === 1){
            /**导演**/
            result.director = parserFigure(figureDetail,$);
        }
    }
    return result;
}

function parserFigure(figureDetail, $) {
    var result = [];

    if(figureDetail === undefined || figureDetail.length === 0){
        return result;
    }
    for (var k = 0; k < figureDetail.length; k++) {

        result.push($(figureDetail[k]).text());
    }

    return result;
}


try{

    log(htmlString,8);
    result = parserHtml();

    parserInfo.index = _INDEX;
    parserInfo.ver = _VER;
    parserInfo.type = _TYPE;

    result.handler = _INDEX;

    if(result.items.length > 0){
        result.retcode = _RETCODE_SUCCESS;
    }else{
        result.retcode = _RETCODE_ERROR;
    }

    log('Parsing res check,Total hits:' + result.totalHits + ', items count:' + result.items.length,
        config.LOG._LOG_LEVEL_WARNING);

}catch(exp){
    log('Parsing exception:' + exp.message, config.LOG._LOG_LEVEL_ERROR);
}
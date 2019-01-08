var _VER = '1.0.0';
var _INDEX = 0;
var _TYPE = 'macro:article_list';

var _RETCODE_SUCCESS = 1;
var _RETCODE_ERROR = -1;


function log(log, level){
    utils.log('{DParser article_list ' + _INDEX + '(ver:' + _VER + ')} ' + log, level);
}


function parseResult($){

    var result = {
        state:1,
        rows:[]
    };

    var articleList = $("div.area_list .col_l").find('.list_item');
    log('articleSize:'+articleList.length,4);
    for (var i = 0; i < articleList.length; i++) {
        var dome = $(articleList[i]);
        var title = dome.find('h2').find('a').text();
        var info = dome.find('p').text();
        var href = dome.find('a').attr('href');
        var image = dome.find('a img').attr('src');
        log('title:'+title,4);
        log('info:'+ info,4);
        log('href:'+ href,4);
        log('image:'+ image,4);
        result.rows.push({
            title:title.trim(),
            info:info.trim(),
            href:href,
            image:image
        })
    }
    return result;
}


try{

    htmlString = iconv.decode(htmlString,'gbk');
    log('Parsing html check,html:' + htmlString, 8);
    parserInfo.index = _INDEX;
    parserInfo.ver = _VER;
    parserInfo.type = _TYPE;
    var $ = cheerio.load(htmlString);
    result.res = parseResult($);

    result.handler = _INDEX;

    if(result.res.state===1){
        result.retcode = _RETCODE_SUCCESS;
    }else{
        result.retcode = _RETCODE_ERROR;
    }

}catch(exp){
    log('Parsing exception:' + exp.message, config.LOG._LOG_LEVEL_ERROR);
}
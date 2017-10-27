function openModalWindow(topSize,width,height){
	var doc_width = 0;
	if (window.top.innerWidth)
		doc_width = window.top.innerWidth;
	else if ((document.top.body) && (document.top.body.clientWidth))
		doc_width = document.top.body.clientWidth;
	

	var width_ = 500;
	if(width){
		width_ = width;
	}
	var height_ = 400;
	if(height){
		height_ = height;
	}
	
	var left = ((doc_width - width_) / 2) + "px";
	var html = "<div id='loginmodal' style='display: block; position: fixed; opacity: 1; z-index: 11000; left: "+left+"; top: 110px;width:"+width+"px;height:"+height_+"px;'>"
	+ "<iframe id='iframe_top_"+topSize+"' name='iframe_top_"+topSize+"' src='/user/add'></iframe>";
	html += "<div style='bottom:15px;right:85px;position:absolute;'><a href='javascript:void(0)' class='button button-little bg-blue' style='padding:5px 15px;' onclick='DelSelect()'>确定</a></div>";
	html += "<div style='bottom:15px;right:15px;position:absolute;'><a href='javascript:void(0)' class='button button-little bg-red' style='padding:5px 15px;' onclick='DelSelect()'>关闭</a></div>";
	html += "</div>";
	$(window.top.document).find("body").append(html);
	html = "<div id='lean_overlay' style='display: block; opacity: 0.45;'></div>";
	$(window.top.document).find("body").append(html);
}
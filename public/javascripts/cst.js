function pushState(li, domain, url, data) {
	if (!history.urls) {
		history.urls = [];// array
	}
	
	history.urls.push({
		li : li,
		domain : domain,
		url : url,
		data : data
	});
	history.index = history.urls.length - 1;
}

function preState() {
	if (history.urls) {
		history.index = history.index - 1;
		if (history.index < 0) {
			history.index = 0;
		}

		var li = history.urls[history.index].li;
		var domain = history.urls[history.index].domain;
		var url = history.urls[history.index].url;
		var data = history.urls[history.index].data;
		getPage(li, domain, url, data, true);
	}
}

function nextState() {
	if (history.urls) {
		history.index = history.index + 1;
		if (history.index > history.urls.length - 1) {
			history.index = history.urls.length - 1;
		}

		var li = history.urls[history.index].li;
		var domain = history.urls[history.index].domain;
		var url = history.urls[history.index].url;
		var data = history.urls[history.index].data;
		getPage(li, domain, url, data, true);
	}
}
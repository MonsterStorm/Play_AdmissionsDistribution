function HashMap() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	}

	/** 取 * */
	this.get = function(key) {
		if (this.containsKey(key)) {
			return entry[key];
		} else {
			return null;
		}
	}

	/** 删除 * */
	this.remove = function(key) {
		if (delete entry[key]) {
			size--;
		}
	}

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	}

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for ( var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	}

	/** 所有 Value * */
	this.values = function() {
		var values = new Array(size);
		for ( var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	}

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array(size);
		for ( var prop in entry) {
			keys.push(prop);
		}
		return keys;
	}

	/** Map Size * */
	this.size = function() {
		return size;
	}
}

function pushState(li, domain, url, data) {
	if (!history.urls) {
		history.urls = [];// array
		history.maps = new HashMap();
	}

	var key = domain + url;
	if(history.maps.containsKey(key)) {
		var index = history.maps.get(key);
		//console.dir(key + "===" + index);
		history.urls.splice(index, 1);//删除一个元素
		
		var i = index;
		for(; i < history.urls.length; i++){//更新所有的index
			var tkey = history.urls[i].domain + history.urls[i].url;
			var tindex = history.maps.get(tkey);
			history.maps.put(tkey, tindex - 1);
		}
	}
	//console.dir("++++++++++++++++++++++++");
	//console.dir(history.urls.length);
	history.urls.push({
		li : li,
		domain : domain,
		url : url,
		data : data
	});
	history.index = history.urls.length - 1;
	history.maps.put(key, history.index);
	//console.dir(key + "===" + history.maps.get(key));
	//console.dir(history.urls);
	//console.dir(history.index);
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
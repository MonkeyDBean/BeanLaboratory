function uploadImage(file,url){
  var form = new FormData(); // FormData 对象
  form.append("cover", file); // 文件对象
  form.append("phone", "18840822645");
  xhr=null;
  if (window.XMLHttpRequest){
    xhr=new XMLHttpRequest();
  }else if (window.ActiveXObject){
    xhr=new ActiveXObject("Microsoft.XMLHTTP");
  }
  if (xhr!=null){
    xhr.onreadystatechange=function(){
      if (xhr.readyState==4){
        if (xhr.status==200){
          console.log(1,xhr.response);

        }else{
          console.log(0,xhr.response);
        }
      }
    }
    xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.setRequestHeader("Accept","application/json");
    xhr.send(form); //开始上传，发送form数据
  }else{
    alert("换浏览器！");
  }
}
$('.icon-cheng1-copy').click(function(){
  $('.upload-input').click();
})
$('.upload-input').change(function(){
  var file=this.files[0];
  var url = "http://127.0.0.1:8096/monkey/testtest/avatar/upload"
  uploadImage(file,url);
})
const cross=document.querySelector(".crossbtn");
const sidebar=document.querySelector(".sidebar");
const bar=document.querySelector(".bar")
const content= document.querySelector(".content");

cross.addEventListener("click",(e)=>{
bar.style.display="block";
sidebar.style.display="none";
content.style.margin="0";
content.style.width="100%";
cross.style.display="none";
content.style.transition="ease 0.2s";

})

bar.addEventListener("click",(e)=>{
bar.style.display="none";
sidebar.style.display="block";
content.style.margin="0 0 0 20%";
sidebar.style.width="20%";
cross.style.display="block";
content.style.width="80%"

})
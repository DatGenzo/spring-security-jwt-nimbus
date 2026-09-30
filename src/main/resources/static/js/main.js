const KEY="jwtNimbusToken";
const esc=s=>String(s??"").replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[c]));
async function json(r){const t=await r.text();try{return JSON.parse(t)}catch{return{message:t}}}
function msg(id,type,text){document.getElementById(id).innerHTML=`<div class="alert alert-${type}">${esc(text)}</div>`}
async function api(url,opt={}){return fetch(url,{...opt,headers:{...(opt.headers||{}),Authorization:`Bearer ${localStorage.getItem(KEY)}`}})}
document.addEventListener("DOMContentLoaded",()=>{
 if(document.body.id==="login-page"){
  if(localStorage.getItem(KEY)){location.href="/profile";return}
  document.getElementById("register-email").value=`student-${Date.now()}@example.com`;
  document.getElementById("login-form").onsubmit=async e=>{e.preventDefault();let r=await fetch("/api/auth/login",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({email:document.getElementById("login-email").value,password:document.getElementById("login-password").value})});let d=await json(r);if(!r.ok){msg("login-message","danger",d.message);return}localStorage.setItem(KEY,d.token);location.href="/profile"};
  document.getElementById("register-form").onsubmit=async e=>{e.preventDefault();let p={fullName:document.getElementById("register-name").value,email:document.getElementById("register-email").value,password:document.getElementById("register-password").value};let r=await fetch("/api/auth/signup",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(p)});let d=await json(r);if(!r.ok){msg("register-message","danger",d.message);return}msg("register-message","success","Đăng ký thành công");document.getElementById("login-email").value=d.email};
 }
 if(document.body.id==="profile-page"){
  if(!localStorage.getItem(KEY)){location.href="/login";return}
  document.getElementById("logout").onclick=()=>{localStorage.removeItem(KEY);location.href="/login"};
  (async()=>{let r=await api("/api/users/me"),u=await json(r);if(!r.ok){localStorage.removeItem(KEY);location.href="/login";return}document.getElementById("uid").textContent=u.id;document.getElementById("name").textContent=u.fullName;document.getElementById("email").textContent=u.email;document.getElementById("role").textContent=u.role;if(u.role==="ADMIN"){let x=await api("/api/users"),list=await json(x);document.getElementById("admin").classList.remove("d-none");document.getElementById("users").innerHTML=list.map(v=>`<tr><td>${esc(v.id)}</td><td>${esc(v.fullName)}</td><td>${esc(v.email)}</td><td>${esc(v.role)}</td></tr>`).join("")}})();
 }
});

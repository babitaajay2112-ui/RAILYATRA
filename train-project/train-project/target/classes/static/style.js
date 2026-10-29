(() => {
  "use strict";
  const $ = s => document.querySelector(s), $$ = s => [...document.querySelectorAll(s)];
  const KEY = "railyatra_demo_bookings_v2";
  const trains = [
    {id:"12002",name:"Shatabdi Express",from:"New Delhi",to:"Lucknow",depart:"06:10",arrive:"12:25",duration:375,sl:690,a3:1450,a2:1980,a1:3290,cc:980,s2:320,days:"Daily",tag:"Fast connection"},
    {id:"12952",name:"Mumbai Rajdhani",from:"New Delhi",to:"Mumbai",depart:"16:55",arrive:"08:35",duration:940,sl:0,a3:2450,a2:3420,a1:5680,cc:0,s2:0,days:"Daily",tag:"Popular route"},
    {id:"12015",name:"Ajmer Shatabdi",from:"New Delhi",to:"Jaipur",depart:"06:05",arrive:"10:40",duration:275,sl:0,a3:0,a2:0,a1:0,cc:850,s2:290,days:"Daily",tag:"Quick trip"},
    {id:"12424",name:"Dibrugarh Rajdhani",from:"New Delhi",to:"Kolkata",depart:"16:20",arrive:"10:00",duration:1060,sl:0,a3:2320,a2:3220,a1:5320,cc:0,s2:0,days:"Sample schedule",tag:"Long-distance"},
    {id:"12556",name:"Gorakhdham Express",from:"New Delhi",to:"Lucknow",depart:"20:00",arrive:"02:40",duration:400,sl:490,a3:1320,a2:1810,a1:0,cc:0,s2:260,days:"Daily",tag:"Overnight"},
    {id:"12138",name:"Punjab Mail",from:"New Delhi",to:"Mumbai",depart:"05:15",arrive:"07:35",duration:1580,sl:620,a3:1680,a2:2360,a1:0,cc:0,s2:300,days:"Daily",tag:"Classic route"},
    {id:"12050",name:"Gatimaan Express",from:"Agra",to:"New Delhi",depart:"17:45",arrive:"19:30",duration:105,sl:0,a3:0,a2:0,a1:0,cc:750,s2:0,days:"Sample schedule",tag:"Express"},
    {id:"12956",name:"Jaipur Superfast",from:"Mumbai",to:"Jaipur",depart:"19:10",arrive:"14:15",duration:1145,sl:560,a3:1520,a2:2110,a1:0,cc:0,s2:280,days:"Daily",tag:"Popular route"},
    {id:"12627",name:"Karnataka Express",from:"New Delhi",to:"Bengaluru",depart:"20:20",arrive:"06:40",duration:1820,sl:780,a3:2090,a2:2940,a1:0,cc:0,s2:360,days:"Daily",tag:"Long-distance"},
    {id:"12604",name:"Chennai Express",from:"Mumbai",to:"Chennai",depart:"20:35",arrive:"03:15",duration:1240,sl:570,a3:1540,a2:2180,a1:0,cc:0,s2:280,days:"Daily",tag:"Overnight"},
    {id:"12156",name:"Bhopal Express",from:"Bhopal",to:"New Delhi",depart:"21:00",arrive:"07:15",duration:615,sl:510,a3:1380,a2:1900,a1:0,cc:0,s2:270,days:"Daily",tag:"Overnight"},
    {id:"14205",name:"Ayodhya Express",from:"Lucknow",to:"Varanasi",depart:"07:20",arrive:"13:05",duration:345,sl:340,a3:940,a2:1320,a1:0,cc:0,s2:190,days:"Daily",tag:"Regional"},
    {id:"12004",name:"Lucknow Shatabdi",from:"Lucknow",to:"New Delhi",depart:"15:30",arrive:"22:10",duration:400,sl:0,a3:0,a2:0,a1:0,cc:960,s2:330,days:"Daily",tag:"Fast connection"},
    {id:"12802",name:"Purushottam Express",from:"New Delhi",to:"Kolkata",depart:"22:10",arrive:"04:50",duration:1000,sl:540,a3:1460,a2:2050,a1:0,cc:0,s2:270,days:"Daily",tag:"Overnight"},
    {id:"12724",name:"Telangana Express",from:"New Delhi",to:"Hyderabad",depart:"16:00",arrive:"10:30",duration:1110,sl:650,a3:1760,a2:2460,a1:0,cc:0,s2:310,days:"Daily",tag:"Long-distance"},
    {id:"12006",name:"Kalka Shatabdi",from:"New Delhi",to:"Amritsar",depart:"07:20",arrive:"13:50",duration:390,sl:0,a3:0,a2:0,a1:0,cc:890,s2:300,days:"Daily",tag:"Day train"}
  ];
  const classes={SL:"Sleeper (SL)","3A":"AC 3 Tier (3A)","2A":"AC 2 Tier (2A)","1A":"AC First Class (1A)",CC:"Chair Car (CC)","2S":"Second Sitting (2S)"};
  let filter="all", search={from:"New Delhi",to:"Mumbai",date:"",travelClass:"SL"}, seat=null, toastTimer;
  const localDate=()=>{const d=new Date();return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,"0")}-${String(d.getDate()).padStart(2,"0")}`};
  const money=n=>n?"₹"+Number(n).toLocaleString("en-IN"):"Not offered";
  const mins=t=>{const [h,m]=t.split(":").map(Number);return h*60+m};
  const duration=n=>`${Math.floor(n/60)}h ${String(n%60).padStart(2,"0")}m`;
  const dateText=s=>s?new Date(s+"T12:00:00").toLocaleDateString("en-IN",{day:"numeric",month:"short",year:"numeric"}):"Date not set";
  const esc=s=>String(s).replace(/[&<>"']/g,c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
  function bookings(){try{return JSON.parse(localStorage.getItem(KEY)||"[]")}catch{return []}}
  function save(list){localStorage.setItem(KEY,JSON.stringify(list))}
  function toast(msg){const t=$("#toast");t.textContent=msg;t.classList.add("show");clearTimeout(toastTimer);toastTimer=setTimeout(()=>t.classList.remove("show"),3000)}
  function fare(t,c){return Number(t[c.toLowerCase()]||0)}
  function matching(){
    let a=trains.filter(t=>t.from===search.from&&t.to===search.to&&fare(t,search.travelClass)>0);
    if(filter==="morning")a=a.filter(t=>mins(t.depart)<720);
    if(filter==="day")a=a.filter(t=>mins(t.depart)>=720&&mins(t.depart)<1080);
    if(filter==="night")a=a.filter(t=>mins(t.depart)>=1080||mins(t.depart)<360);
    const s=$("#sortBy").value;a.sort((x,y)=>s==="price"?fare(x,search.travelClass)-fare(y,search.travelClass):s==="duration"?x.duration-y.duration:mins(x.depart)-mins(y.depart));return a;
  }
  function renderTrains(){
    const list=matching();$("#trainCount").textContent=`${list.length} train${list.length===1?"":"s"}`;
    $("#resultSubtitle").textContent=`${search.from} → ${search.to} · ${dateText(search.date)} · ${classes[search.travelClass]}`;
    $("#emptyResults").classList.toggle("hidden",list.length>0);$("#trainResults").classList.toggle("hidden",!list.length);
    $("#trainResults").innerHTML=list.map(t=>`<article class="train-card"><div><div class="train-name">${esc(t.name)}</div><div class="train-number">Train no. ${t.id} · ${esc(t.days)}</div><span class="train-tag">${esc(t.tag)}</span></div><div class="route-line"><div class="station-time"><strong>${t.depart}</strong><span>${esc(t.from)}</span></div><div class="route-track"><small>${duration(t.duration)}</small><div class="line"></div><small>${mins(t.arrive)<mins(t.depart)?"+1 day":"Same day"}</small></div><div class="station-time"><strong>${t.arrive}</strong><span>${esc(t.to)}</span></div></div><div class="fare-block"><strong>${money(fare(t,search.travelClass))}</strong><span>per passenger</span><div class="availability">● Demo seats available</div></div><div class="card-action"><button class="search-submit book-train" data-id="${t.id}">Book seat →</button><small>Sample schedule</small></div></article>`).join("");
    $$(".book-train").forEach(b=>b.addEventListener("click",()=>openBooking(b.dataset.id)));
  }
  function doSearch(e){if(e)e.preventDefault();search={from:$("#fromStation").value,to:$("#toStation").value,date:$("#journeyDate").value,travelClass:$("#travelClass").value};if(search.from===search.to){toast("Choose two different stations.");return}if(!search.date){toast("Select a journey date.");return}filter="all";$$(".filter-chip").forEach(b=>b.classList.toggle("active",b.dataset.filter==="all"));renderTrains();$("#trains").scrollIntoView({behavior:"smooth"})}
  function openBooking(id){
    const t=trains.find(x=>x.id===id);if(!t)return;seat=null;
    $("#selectedTrainId").value=t.id;$("#modalTrainInfo").textContent=`${t.name} (${t.id}) · ${t.from} → ${t.to} · ${dateText(search.date)} · ${classes[search.travelClass]}`;$("#modalFare").textContent=money(fare(t,search.travelClass));$("#bookingForm").reset();$("#selectedTrainId").value=t.id;
    $("#seatGrid").innerHTML=Array.from({length:18},(_,i)=>{const n="S"+String(i+1).padStart(2,"0"),taken=(i*7+t.id.charCodeAt(0))%11===0;return `<button type="button" class="seat ${taken?"taken":""}" data-seat="${n}" ${taken?"disabled":""}>${n}</button>`}).join("");
    $$(".seat:not(.taken)").forEach(b=>b.addEventListener("click",()=>{$$(".seat").forEach(x=>x.classList.remove("selected"));b.classList.add("selected");seat=b.dataset.seat}));
    $("#bookingModal").classList.remove("hidden");document.body.style.overflow="hidden";
  }
  function closeModal(){$("#bookingModal").classList.add("hidden");document.body.style.overflow=""}
  function confirmBooking(e){
    e.preventDefault();if(!seat){toast("Select an available demo seat.");return}
    const t=trains.find(x=>x.id===$("#selectedTrainId").value),name=$("#passengerName").value.trim(),age=Number($("#passengerAge").value),gender=$("#passengerGender").value;
    if(!name||!Number.isInteger(age)||age<1||age>120||!gender){toast("Enter valid passenger details.");return}
    const pnr=String(Math.floor(1000000000+Math.random()*8999999999));
    const b={pnr,trainId:t.id,trainName:t.name,from:t.from,to:t.to,date:search.date,travelClass:search.travelClass,fare:fare(t,search.travelClass),passengerName:name,age,gender,seat,status:"Confirmed"};
    save([b,...bookings()]);closeModal();renderBookings();toast("Demo booking confirmed. PNR: "+pnr);$("#pnrInput").value=pnr;checkPnr();
  }
  function renderBookings(){
    const all=bookings();$("#noBookings").classList.toggle("hidden",all.length>0);$("#bookingList").classList.toggle("hidden",!all.length);
    $("#bookingList").innerHTML=all.map(b=>`<article class="booking-card"><div class="booking-top"><div><h3>${esc(b.trainName)}</h3><p>Train ${esc(b.trainId)} · PNR ${esc(b.pnr)}</p></div><span class="status-pill ${b.status==="Cancelled"?"cancelled":""}">${esc(b.status)}</span></div><div class="booking-route"><div><strong>${esc(b.from)}</strong><span>FROM</span></div><span class="arrow">→</span><div style="text-align:right"><strong>${esc(b.to)}</strong><span>TO</span></div></div><div class="booking-meta"><div>JOURNEY DATE<strong>${dateText(b.date)}</strong></div><div>PASSENGER<strong>${esc(b.passengerName)} · ${b.age}</strong></div><div>CLASS / SEAT<strong>${esc(classes[b.travelClass]||b.travelClass)} · ${esc(b.seat)}</strong></div><div>PNR<strong>${esc(b.pnr)}</strong></div></div><div class="booking-actions"><strong>${money(b.fare)}</strong>${b.status==="Confirmed"?`<button class="text-button cancel-booking" data-pnr="${b.pnr}">Cancel demo booking</button>`:"<span style='font-size:10px;color:#9aa59e'>Cancelled</span>"}</div></article>`).join("");
    $$(".cancel-booking").forEach(b=>b.addEventListener("click",()=>{if(confirm("Cancel this demo booking?")){save(bookings().map(x=>x.pnr===b.dataset.pnr?{...x,status:"Cancelled"}:x));renderBookings();toast("Demo booking cancelled.")}}));
  }
  function checkPnr(e){if(e)e.preventDefault();const p=$("#pnrInput").value.trim(),b=bookings().find(x=>x.pnr===p),r=$("#pnrResult");r.classList.remove("hidden");if(!b){r.style.background="#fff0ed";r.style.color="#b6473f";r.textContent="No matching demo PNR. Check the number or make a demo booking.";return}r.style.background="#edf8f1";r.style.color="#1b7351";r.textContent=`PNR ${b.pnr} · ${b.status} · ${b.trainName} · ${b.from} → ${b.to} · ${dateText(b.date)} · Seat ${b.seat}`}
  $("#journeyDate").min=localDate();$("#journeyDate").value=localDate();
  $("#searchForm").addEventListener("submit",doSearch);
  $("#swapStations").addEventListener("click",()=>{const a=$("#fromStation"),b=$("#toStation"),v=a.value;a.value=b.value;b.value=v});
  $("#sortBy").addEventListener("change",renderTrains);
  $$(".filter-chip").forEach(b=>b.addEventListener("click",()=>{filter=b.dataset.filter;$$(".filter-chip").forEach(x=>x.classList.toggle("active",x===b));renderTrains()}));
  $$(".route-tile").forEach(b=>b.addEventListener("click",()=>{$("#fromStation").value=b.dataset.from;$("#toStation").value=b.dataset.to;$("#search").scrollIntoView({behavior:"smooth",block:"center"});toast("Route selected. Choose a date and search.")}));
  $("#bookingForm").addEventListener("submit",confirmBooking);$("#closeModal").addEventListener("click",closeModal);
  $("#bookingModal").addEventListener("click",e=>{if(e.target===$("#bookingModal"))closeModal()});document.addEventListener("keydown",e=>{if(e.key==="Escape")closeModal()});
  $("#pnrForm").addEventListener("submit",checkPnr);$("#refreshBookings").addEventListener("click",()=>{renderBookings();toast("Bookings refreshed.")});
  $("#jumpPnr").addEventListener("click",()=>$("#pnr").scrollIntoView({behavior:"smooth"}));
  $("#menuToggle").addEventListener("click",()=>$("#nav").classList.toggle("open"));
  $$("#nav a").forEach(a=>a.addEventListener("click",()=>{$$("#nav a").forEach(n=>n.classList.remove("active"));a.classList.add("active");$("#nav").classList.remove("open")}));
  renderTrains();renderBookings();
})();
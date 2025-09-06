function toggleNav(){
  const btn = document.querySelector('.nav-toggle');
  const nav = document.getElementById('site-nav');
  const expanded = btn.getAttribute('aria-expanded') === 'true';
  btn.setAttribute('aria-expanded', String(!expanded));
  nav.classList.toggle('open');
}

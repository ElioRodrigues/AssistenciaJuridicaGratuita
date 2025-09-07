function toggleNav(){
  const btn = document.querySelector('.nav-toggle');
  const nav = document.getElementById('site-nav');
  const expanded = btn.getAttribute('aria-expanded') === 'true';
  btn.setAttribute('aria-expanded', String(!expanded));
  nav.classList.toggle('open');
}

// máscaras simples (opcional)
function maskCEP(v){ return v.replace(/\D/g,'').replace(/^(\d{5})(\d{0,3}).*/, '$1-$2'); }
function maskCPF(v){ return v.replace(/\D/g,'').replace(/(\d{3})(\d)/, '$1.$2')
                             .replace(/(\d{3})(\d)/, '$1.$2')
                             .replace(/(\d{3})(\d{1,2})$/, '$1-$2'); }
function maskPhone(v){ return v.replace(/\D/g,'')
  .replace(/^(\d{2})(\d)/, '($1) $2')
  .replace(/(\d{5})(\d{4}).*/, '$1-$2'); }

['cep','cpf','telefone'].forEach(id=>{
  const el = document.getElementById(id);
  if(!el) return;
  el.addEventListener('input', e=>{
    const t=e.target;
    if(id==='cep') t.value = maskCEP(t.value);
    if(id==='cpf') t.value = maskCPF(t.value);
    if(id==='telefone') t.value = maskPhone(t.value);
  });
});

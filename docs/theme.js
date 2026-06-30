(function () {
  function current() {
    return document.documentElement.getAttribute('data-theme') || 'light';
  }
  function label() {
    var b = document.getElementById('theme-toggle');
    if (b) b.textContent = current() === 'dark' ? '☀' : '☾';
  }
  window.toggleTheme = function () {
    var next = current() === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    try { localStorage.setItem('jf-theme', next); } catch (e) {}
    label();
  };
  if (document.readyState !== 'loading') label();
  else document.addEventListener('DOMContentLoaded', label);
})();

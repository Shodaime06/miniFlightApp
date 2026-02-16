const Auth = (() => {
  const ACCESS_KEY = "access_token";

  function getAccess() {
    return sessionStorage.getItem(ACCESS_KEY);
  }
  function setAccess(token) {
    sessionStorage.setItem(ACCESS_KEY, token);
  }
  function clearAccess() {
    sessionStorage.removeItem(ACCESS_KEY);
  }

  async function register(email, password) {
    const r = await fetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });
    if (!r.ok) throw new Error(await safeText(r) || "Register failed");
  }

  async function login(email, password) {
    const r = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });
    if (!r.ok) throw new Error(await safeText(r) || "Login failed");
    const data = await r.json();
    setAccess(data.accessToken);
  }

  async function refresh() {
    const r = await fetch("/api/auth/refresh", { method: "POST" });
    if (!r.ok) throw new Error("Refresh failed");
    const data = await r.json();
    setAccess(data.accessToken);
    return data.accessToken;
  }

  async function logout() {
    await fetch("/api/auth/logout", { method: "POST" });
    clearAccess();
  }

  async function api(url, options = {}) {
    const opts = { ...options, headers: { ...(options.headers || {}) } };
    const token = getAccess();
    if (token) opts.headers["Authorization"] = "Bearer " + token;

    let r = await fetch(url, opts);

    if (r.status === 401) {
      await refresh();
      const token2 = getAccess();
      if (token2) opts.headers["Authorization"] = "Bearer " + token2;
      r = await fetch(url, opts);
    }

    if (!r.ok) throw new Error(await safeText(r) || ("API error: " + r.status));
    const ct = r.headers.get("content-type") || "";
    if (ct.includes("application/json")) return await r.json();
    return await r.text();
  }

  async function safeText(res) {
    try { return await res.text(); } catch { return ""; }
  }

  return { register, login, refresh, logout, api };
})();

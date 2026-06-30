# Contributing

`java-foundations` is a growing, open collection of core Java building blocks explained
from scratch. New **concepts** and **interactive visualizations** are welcome — drop a file,
open a PR, and it appears on the [live site](https://aidarkazybekov.github.io/java-foundations/)
automatically. No manual index editing.

## How auto-discovery works

A CI step (`docs/gen-manifest.mjs`) scans `docs/concepts/` and `docs/visualizations/`, extracts
each item's title and one-line description, and regenerates `docs/content.json`. The landing page
renders its cards from that manifest. So **adding a file is enough** — the card shows up on the
next deploy.

## Add a concept

1. Create `docs/concepts/<your-slug>.md`.
2. Start it with a level-1 heading and a one-line blockquote summary:
   ```markdown
   # Your concept title

   > One sentence shown as the card description and why it matters.

   ...the rest of the explanation...
   ```
   - The `# ...` becomes the card **title**, the `> ...` becomes the card **description**.
3. (Optional) link related code or other concepts.
4. Open a PR. It appears under **Concept deep-dives** automatically.

## Add a visualization

1. Create `docs/visualizations/<your-name>.html` from this skeleton:
   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>Your title — java-foundations</title>
   <script>(function(){try{var t=localStorage.getItem('jf-theme')||(matchMedia('(prefers-color-scheme:dark)').matches?'dark':'light');document.documentElement.setAttribute('data-theme',t);}catch(e){}})();</script>
   <link rel="stylesheet" href="style.css">
   </head>
   <body>
   <div class="topbar">
     <a class="brand" href="../index.html"><span class="dot"></span>← java-foundations</a>
     <button id="theme-toggle" class="theme-toggle" onclick="toggleTheme()" aria-label="toggle theme">☾</button>
   </div>
   <h1>Your title</h1>
   <p class="lead">One line shown as the card description.</p>

   <!-- your interactive widget here; use the shared CSS variables (var(--color-*)) -->

   <script src="../theme.js"></script>
   </body>
   </html>
   ```
   - The `<h1>` becomes the card **title**, the `<p class="lead">` becomes the **description**.
   - Use the shared `var(--color-*)` variables so it works in light **and** dark mode.
2. Open a PR. It appears under **Interactive visualizations** automatically.

## Contributing code (a new kata)

- Keep it **from scratch** and educational (no third-party libraries for the core).
- Add a focused JUnit suite (`mvn test` must stay green). Use Testcontainers, not H2, for anything DB.
- Add a matching concept doc and, ideally, a visualization.

## Local preview

```bash
node docs/gen-manifest.mjs      # refresh content.json from your files
# then serve docs/ (concept cards use fetch, so use a server, not file://):
python3 -m http.server -d docs 8000   # open http://localhost:8000
```

# clojuredocs-helix
Reimplementation of clojuredocs.org using helix and refx.

## Why
Only educational and for personal study, I also have no idea what I'm doing ~~please send help~~.

## How
The static jsons with the analysis are generated manually by [rafaeldelboni/clojure-document-extractor](https://github.com/rafaeldelboni/clojure-document-extractor)

## Commands

### Watch Dev/Tests
Build the developer build and start shadow-cljs watching and serving main in [`localhost:8000`](http://localhost:8000) and tests in [`localhost:8100`](http://localhost:8100)
```bash
npm run watch
```

### CI Tests
Run **Karma** tests targeted for running CI tests with *Headless Chromium Driver*
```bash
npm run ci:tests
```

### Release
Build the release package to production deploy
```bash
npm run release
```
## License

Copyright © 2023 Rafael Delboni

This is free and unencumbered software released into the public domain. For more information, please refer to http://unlicense.org

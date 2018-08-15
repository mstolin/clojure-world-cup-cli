# World Cup CLI
[![Build Status](https://travis-ci.org/mstolin/clojure-world-cup-cli.svg?branch=master)](https://travis-ci.org/mstolin/clojure-world-cup-cli)

This is a command line interface for the FIFA world cup 2018 written in clojure. I developed this project for a university project.

## Installation

- Download or clone this repository.
- Run `lein bin`. after that you can run `target/cup COMMAND [option]`.
- Or you can run `lein run -- COMMAND [option]`.

## Usage

```
A simple command line interface for the fifa world cup 2018.

Usage: cup COMMAND [option]

Options:
  -n, --name NAME  false  The name of a specific group, team or stadium
  -i, --id ID             The id of a specific team or stadium
  -h, --help              You are using this option right now :)

Commands:
  final                   Shows the final game
  group                   Shows a specific group (a - h)
  knockout                Shows the knockout phase
  play-off                Shows the play-off
  quarter-final           Shows the quarter final
  round-of-16             Shows the round-of-16
  semi-final              Shows the semi final
  stadium                 Shows a specific stadium
  team                    Shows a specific team
```

## Tests

Just run `lein test`.

## License

[MIT](https://github.com/mstolin/clojure-world-cup-cli/blob/master/LICENSE)

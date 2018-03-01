# Project Conventions

## Branch Names

Branch names should be all lowercase, and any spaces between words should be
single hyphens.  Uppercase letters should be avoided as it makes typing
commands more difficult.

Most branches should be named based on the following pattern:

```
<type>/<name>
```

The three types of branches we've established are the following:

* `feature` : Adding new features or enhancing existing features.

* `refactor` : Restructuring code, without changing many features.

* `environment` : Changing deployment configuration, build systems, etc.

* `bugfix` : Bug fixes.

The only exceptions to this rule are "special" branches, like `master`.  By
having standard conventions for naming branches it makes it easier to
immediately understand what is happening in the branch, without even having to
look at the descriptions of the commits in that branch.  Special branches like
`master` are notated specifically *because* they don't have a prefix.  The only
branches that should just be their bare names are "special" branches like
`master`.  Having prefixes also makes tab-completion more effective when typing
git commands and helps to ensure that the commands you're writing are correct.

## Commit Messages

Commit messages should be full sentences, but they need not be prose.  Just
plain words like "setup service" are bad as they do not describe the work that
was actually done in the commit, nor are they pretty to look at.  Starting with
verbs helps to immediately describe the *kind* of work done, and starting with
a capital letter helps to draw attention to the word, apart from the rest of
the first line.

Examples:

* Changed default threading model to AUTO_DEFERRED.

* Added a proper Makefile and cleaned up Maven warnings.

* Added tool for exporting signed artifact segments of documents.

You don't have to be exactly as descriptive as the above, but commit messages
should try to resemble those.


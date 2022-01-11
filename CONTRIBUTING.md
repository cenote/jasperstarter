## How to contribute to JasperStarter

#### **Did you find a bug?**

* **Are you sure it is really a bug?** If not, ask at the
  [Forum](https://sourceforge.net/p/jasperstarter/discussion/). Here
  you can also get help from other users if you have any questions.

* **Ensure the bug was not already reported** by searching existing 
  [Issues](https://cenote-issues.atlassian.net/jira/software/c/projects/JAS).

* If you're unable to find an open issue addressing the problem, open
  a new one. Be sure to include a **title and clear description**, as
  much relevant information as possible, and a **report and data
  sample** demonstrating the expected behavior that is not occurring.
  You can use one of the sample reports and modify it for that.
  
#### **Do you have an idea or whish for an improvement or a new feature?**

* **Create a new issue** and set the issue type to "Improvement" or
  "New Feature" depending on what fits best.
  
* If you want to contribute to your newly created feature request,
  please write a comment about to get into discussion first.

#### **Do you want to contribute code?**

Great, I really need some help.

To make my life easier and to prevent yourself from being frustrated
from not accepted pull requests, here are some rules to follow:

* Any code you want to contribute should end up in a pull request on
  bitbucket.org that must be related to only one issue.

* Create a branch with a name consisting of the issue number and title
  in snake case. F.e. `jas_0815_some_foo`. Long titles can be
  shortened.
  
* Any commit in that branch should contain the issue number (key) at
  the beginning of the line like `JAS-0815 my commit message` so it
  can be linked to the issue in Jira. Respect usual git commit message
  rules.

* There is a big chance, that I find some time to review your pull
  request **after** I did some work on the project. I don't like to
  run into a merge conflict while testing your change. So it's a good
  idea to merge the main branch into your change before creating a
  pull request and ...

* **Do not make any change that is not necessary to fullfill the
  issue!** Changes that are unrelated or cosmetic in nature and do not
  add anything substantial to the stability, functionality, or
  testability of JasperStarter will generally not be accepted.
  
* **Do not fiddle with pom.xml !** Most dependencies here are related
  to the JasperReports Library and may change if I add a new version
  of the library or for security reasons. So you may add a new
  dependency for your new code which depends on it or you may change
  the structure if you contribute to an build system related issue
  only.
  
* **Write code that compile with Java7** This is especially important
  for bugfixes that may be also relevant to the Java7 branch. New
  features that are not usable with JasperReports <= v6.4.0 may be
  written for **Java8**.

You feel that this rules are too strict, complex or limiting your
creativity? Maybe, but my time for this project is very limited and I
get not payed for it. There are big projects out there, that have also
strict rules because it makes sense. If you are interested you can
read for example about a [refused pull
request](https://github.com/rails/rails/pull/13771#issuecomment-32746700)
in the "Ruby on Rails" project.
I borrowed some sentences from their contributions.md


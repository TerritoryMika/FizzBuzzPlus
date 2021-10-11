# FizzBuzz+
![stolen from google](https://code.kx.com/q/img/fizzbuzz.png)

Have you been asked for doing FizzBuzz during interview?

well, not for me
cause i am still a student lol

Anyway, `FizzBuzz+` is a language where its only function is to do FizzBuzz!

not very exciting, right? yea this is just a bad joke ending up wasting 15 mins of my life lol

## How to use

```
15:FizzBuzz!
5:Buzz!
3:Fizz!
```
this will be your regular FizzBuzz program, formed by 3 `conditions`, which is just saying :
```
if mod 15 has no remainder, print "FizzBuzz!"
if mod 5 has no remainder, print "Buzz!"
if mod 3 has no remainder, print "Fizz!"
```
a condition can be seen as
```
n[|r]:p
if [current value] mod {n} ,remainder = {r}, print {p}
```
for instance, `15:FizzBuzz!` means *if mod 15 has no remainder, print "FizzBuzz!"* and `7|2:Monkey!`  means *if mod 7, remainder equals 2, print "Monkey!"*


*\*note that "FizzBuzz!" will override "Buzz!" and "Fizz!" as it is in higher position*

here is a few thing i implemented :

`~` can be used to represent current value

`^` can be used to represent the max value

`&` can connect the expression part and the string part


## Explanation for example

here are few example i writen in `FizzBuzz+`

### [Hello, World!](https://esolangs.org/wiki/Hello,_world!)
```
1:Hello, World!
```

### [Truth-machine](https://esolangs.org/wiki/Truth-machine)
```
1:1
```
*\*just put in a large number and pretend it is infinite*

### Double Up
```
1:~+~
```

### End
```
^:this is the end of the program, bye bye!
```

### [99 bottles of beer](https://esolangs.org/wiki/99_bottles_of_beer)
```
^:1 bottle of beer on the wall,\n1 bottle of beer.\nTake one down, pass it around,\nNo bottles of beer on the wall.
1:(^-~)& bottles of beer on the wall,\n&(^-~)& bottles bottle of beer.\nTake one down, pass it around,\n&(^-~)& bottles bottles of beer on the wall.
```

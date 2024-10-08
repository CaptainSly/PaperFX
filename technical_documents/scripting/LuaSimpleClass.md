# Implementation of class()
http://lua-users.org/wiki/SimpleLuaClasses

class() uses two tricks. It allows you to construct a class using the call notation (like Dog('fido') above) by giving the class itself a metatable which defines __call. It handles inheritance by copying the fields of the base class into the derived class. This isn't the only way of doing inheritance; we could make __index a function which explicitly tries to look a function up in the base class(es). But this method will give better performance, at a cost of making the class objects somewhat fatter. Each derived class does keep a field _base that contains the base class, but this is to implement is_a. 

```lua
-- class.lua
-- Compatible with Lua 5.1 (not 5.0).
function class(base, init)
   local c = {}    -- a new class instance
   if not init and type(base) == 'function' then
      init = base
      base = nil
   elseif type(base) == 'table' then
    -- our new class is a shallow copy of the base class!
      for i,v in pairs(base) do
         c[i] = v
      end
      c._base = base
   end
   -- the class will be the metatable for all its objects,
   -- and they will look up their methods in it.
   c.__index = c

   -- expose a constructor which can be called by <classname>(<args>)
   local mt = {}
   mt.__call = function(class_tbl, ...)
   local obj = {}
   setmetatable(obj,c)
   if init then
      init(obj,...)
   else 
      -- make sure that any stuff from the base class is initialized!
      if base and base.init then
      base.init(obj, ...)
      end
   end
   return obj
   end
   c.init = init
   c.is_a = function(self, klass)
      local m = getmetatable(self)
      while m do 
         if m == klass then return true end
         m = m._base
      end
      return false
   end
   setmetatable(c, mt)
   return c
end
```

-------------------------------------------------------------------------------


TO USE WITH PAPER FOLLOW THE FOLLOWING SYNTAX

```lua
Animal = class(function(a,name)
   a.name = name
end)

function Animal:__tostring()
  return self.name..': '..self:speak()
end

Dog = class(Animal)

function Dog:speak()
  return 'bark'
end

Cat = class(Animal, function(c,name,breed)
         Animal.init(c,name)  -- must init base!
         c.breed = breed
      end)

function Cat:speak()
  return 'meow'
end

Lion = class(Cat)

function Lion:speak()
  return 'roar'
end
```
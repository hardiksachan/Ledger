# Domain

- All money will be stored in INR cause I don't want to deal with the complexity of managing 
different currency yet. Also, I'll store it as Long (Paise) and not as Float (Rupee)
  
- Colors stored in database are kind of category tags. They will later be mapped to Color in UI.

- All IDs stored in Db are Longs

- [TODO] I'm not storing files yet. I'll figure that out later

# Data

- Since `transaction` is  a reserved keyword in sqldelight, `ledgerEntry` is used instead

- I have to figure out how to do paging of transactions. Options: Paging3 or a custom impl with flows or something

- Make an in-memory cache for categories and modes.
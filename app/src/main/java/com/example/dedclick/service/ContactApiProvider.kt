package com.example.dedclick.service

import com.example.dedclick.data.model.ContactDto

object ContactApiProvider {

    private val contactService: ContactService by lazy {
        RetrofitProvider.retrofit.create(ContactService::class.java)
    }

    suspend fun getContacts(token:String) : ApiResult<List<ContactDto>>{
        return try{
            val response = contactService.getContacts("Bearer $token")

            if(response.isSuccessful){
                val list = response.body()

                if(list!=null){
                    for (contact in list){
                        contact.keeper.roleName = "trusted"
                        contact.member.roleName = "elder"
                    }

                    ApiResult.Success(list)
                }else{
                    ApiResult.Error(-1, "Empty response body")
                }
            }else{
                ApiResult.Error(
                    response.code(),
                    response.errorBody()?.string()
                )
            }
        } catch (e:Exception){
            ApiResult.Error(-1, e.message)
        }
    }

    suspend fun deleteContact(id: Long, token: String) : ApiResult<Unit>{
        return try{
            val response = contactService.deleteContact(id, "Bearer $token")
            if(response.isSuccessful){
                ApiResult.Success(Unit)
            }else{
                ApiResult.Error(response.code(), response.errorBody()?.string())
            }
        }catch (e:Exception){
            ApiResult.Error(-1, e.message)
        }
    }

}
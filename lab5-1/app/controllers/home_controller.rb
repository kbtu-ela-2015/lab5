class HomeController < ApplicationController

	def about

	end

  def lab5
    type = params[:type]
    value = params[:value]
    value1 = params[:value1]
    if type == 'exponentiation'
      value = value.to_s+" "+value1.to_s
    end

    Publisher.publish("calcs", type.to_s , value.to_s)

    @s = {}
    @s[:status] = value
    render json: @s
  end

end

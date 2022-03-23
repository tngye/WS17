package sg.edu.nus.iss.WS17.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.WS17.model.Currency;
import sg.edu.nus.iss.WS17.services.CurrencyService;

@Controller
@RequestMapping("/")
public class CurrencyController {

    @Autowired CurrencyService cSvc;
    
    List<Currency> currencyList = new ArrayList<Currency>();

    @GetMapping
    public String getCurrencyList(Model model){

        currencyList = cSvc.getCurrencyList();
        
        System.out.println("Success!:" + currencyList);
        model.addAttribute("currencyList", currencyList);

        
        return "index";
    }

    @GetMapping("/convert")
    public String convert(@RequestParam MultiValueMap<String, String> form, Model model){
        String from = form.getFirst("from");
        String[] arrofFrom = from.split("-");
        String fromId= arrofFrom[0];
        String fromName = arrofFrom[1];
        String fromSym = arrofFrom[2];

        System.out.println(">>>>fron1: " + fromId);
        System.out.println(">>>>arrofFrom[0]: " + arrofFrom[0]);

        String to = form.getFirst("to");
        String[] arrofTo = to.split("-");
        String toId = arrofTo[0];
        String toName = arrofTo[1];
        String toSym = arrofTo[2];
        Double amt = Double.parseDouble(form.getFirst("amt"));

        Double convertedAmt = cSvc.getConversion(fromId, toId, amt);
        model.addAttribute("convertedAmt", convertedAmt);

        // Iterator<Currency> itr = currencyList.iterator();
        // System.out.println(">>>From : " + currencyList);
        // String fromName = null;
        // String toName = null;
        // while(itr.hasNext()){
        //     if(from ==  itr.next().getCurrencyId()){
        //         fromName = itr.next().getCurrencyName();
        //         System.out.println(">>>From : " + fromName);
        //     }
        // }
        // while(itr.hasNext()){
        //     if(to ==  itr.next().getCurrencyId()){
        //         toName = itr.next().getCurrencyName();
        //     }
        // }
        // System.out.println(">>>From : " + fromName);
        model.addAttribute("fromName", fromName);
        model.addAttribute("toName", toName);
        model.addAttribute("amt", amt);
        model.addAttribute("toSym", toSym);
        model.addAttribute("fromSym", fromSym);
        return "convert";
    }
}

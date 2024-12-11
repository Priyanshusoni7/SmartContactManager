
let currentTheme = getTheme();

changeTheme();

function changeTheme(){
    document.querySelector("html").classList.add(currentTheme);

    const button = document.querySelector("#theme_change_button")

    button.addEventListener("click", () => {

        let oldTheme = currentTheme;        
        if(currentTheme === "light"){
            currentTheme = "dark";
        }else{
            currentTheme = "light";
        }

        //update localStorage
        setTheme(currentTheme);

        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme); 

        //change the text
        button.querySelector("span").innerText = currentTheme === "light" ? "Dark" : "Light";
    });
}
  
function setTheme(theme)
{
    localStorage.setItem("theme",theme)
}

function getTheme()
{
    let theme =  localStorage.getItem("theme");   
    return theme ? theme : "light";
}